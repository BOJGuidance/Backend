package com.boj.guidance.service.implement;

import com.boj.guidance.config.auth.PasswordEncoder;
import com.boj.guidance.domain.Member;
import com.boj.guidance.dto.MemberDto.*;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.ProblemRepository;
import com.boj.guidance.repository.SystemLogRepository;
import com.boj.guidance.service.MemberService;
import com.boj.guidance.util.AlgorithmVectorUtil;
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.MemberException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final SystemLogRepository systemLogRepository;
    private final PasswordEncoder passwordEncoder;

    public static int overallSum = AlgorithmVectorUtil.overallSum();

    // 사용자 회원가입 기능 구현
    @Override
    public MemberResponseDto join(MemberJoinRequestDto dto) {
        if (memberRepository.findByLoginId(dto.getLoginId()).isPresent()) { // 회원가입 하려는 사용자 id가 이미 존재하면 ERROR
            throw new MemberException(ResponseCode.MEMBER_JOIN_FAIL);
        }
        Member saved = memberRepository.save(dto.toEntity(passwordEncoder.encrypt(dto.getLoginPassword())));

        return new MemberResponseDto().toResponse(saved);
    }

    // 사용자 로그인 기능 구현
    @Override
    public MemberResponseDto login(MemberLoginRequestDto dto, HttpServletRequest httpRequest) {
        Member member = memberRepository.findMemberByLoginIdAndLoginPassword(
                dto.getLoginId(),
                passwordEncoder.encrypt(dto.getLoginPassword())
        ).orElseThrow(
                () -> new MemberException(ResponseCode.MEMBER_LOGIN_FAIL)
        );

        String ipAddress = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        systemLogRepository.save(new MemberSystemLogDto(member, ipAddress, userAgent).toEntity());
        return new MemberResponseDto().toResponse(member);
    }

    // 백준 사용자 인증 구현
    // 단, solved.ac 페이지에 로그인이 되어 있어야 한다.
    @Override
    public MemberAuthRequestDto authorize() {
        WebClient webClient = WebClient.create();
        WebClientRequestDto dto = webClient.get()
                .uri("https://solved.ac/api/v3/account/verify_credentials")
                .retrieve()
                .bodyToMono(WebClientRequestDto.class)
                .block();

        return dto.getUser();
    }

    // 사용자 권한 변경
    @Override
    public MemberResponseDto changeRole(String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
        );
        member.roleUpdate();
        return new MemberResponseDto().toResponse(memberRepository.save(member));
    }

    // 사용자 스터디그룹 모집 활성화 상태 변경
    @Override
    public MemberResponseDto changeState(String memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
        );
        member.stateUpdate();
        return new MemberResponseDto().toResponse(memberRepository.save(member));
    }

    // 취약 알고리즘 업데이트
    @Override
    public MemberResponseDto updateWeakAlgorithm(String handle, String algorithm) {
        Member member = memberRepository.findByHandle(handle).orElseThrow(
                () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
        );
        member.setWeakAlgorithm(algorithm);
        return new MemberResponseDto().toResponse(memberRepository.save(member));
    }

    // 취약 알고리즘 계산
    public List<String> weakAlgorithm(List<Integer> problems) {
        Map<String, Integer> userCategoryCounts = new HashMap<>();

        for (Integer problemId : problems) {
            List<String> categories = problemRepository.findAlgorithmsById(problemId);
            for (String category : categories) {
                category = category.trim();
                userCategoryCounts.put(category, userCategoryCounts.getOrDefault(category, 0) + 1);
            }
        }

        int totalOverallCount = overallSum;
        int totalUserCount = userCategoryCounts.values().stream().mapToInt(Integer::intValue).sum();

        Map<String, Double> overallCategoryPercentages = new HashMap<>();
        for (Map.Entry<String, Integer> entry : AlgorithmVectorUtil.vector.entrySet()) {
            overallCategoryPercentages.put(entry.getKey(), (entry.getValue() / (double) totalOverallCount) * 100);
        }

        Map<String, Double> userCategoryPercentages = new HashMap<>();
        for (Map.Entry<String, Integer> entry : userCategoryCounts.entrySet()) {
            userCategoryPercentages.put(entry.getKey(), (entry.getValue() / (double) totalUserCount) * 100);
        }

        List<Map.Entry<String, Double>> recommendations = new ArrayList<>();
        for (Map.Entry<String, Double> entry : overallCategoryPercentages.entrySet()) {
            String category = entry.getKey();
            double overallPercentage = entry.getValue();
            double userPercentage = userCategoryPercentages.getOrDefault(category, 0.0);
            if (userPercentage < overallPercentage) {
                recommendations.add(new AbstractMap.SimpleEntry<>(category, overallPercentage - userPercentage));
            }
        }

        recommendations.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        return recommendations.stream()
                .map(Map.Entry::getKey)
                .limit(3)
                .collect(Collectors.toList());
    }

    // 사용자 맞은 문제 크롤링
    @Override
    public void updateDetails() {
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            String handle = member.getHandle();
            ArrayList<Integer> list = new ArrayList<>();

            try {
                Document doc = Jsoup.connect("https://www.acmicpc.net/user/" + handle)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                        .timeout(10 * 1000)
                        .get();

                Elements problemList = doc.select(".problem-list a");
                for (Element problem : problemList) {
                    String problemNumber = problem.text();
                    list.add(Integer.parseInt(problemNumber));
                }
            } catch (IOException e) {
                throw new MemberException(ResponseCode.MEMBER_DETAILS_CRAWLING_FAIL);
            }

            String toSave = weakAlgorithm(list).toString().replace("[", "").replace("]", "").replace(", ", ",");
            member.setWeakAlgorithm(toSave);
            memberRepository.save(member);
        }
    }
}
