package com.boj.guidance.service.implement;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Problem;
import com.boj.guidance.dto.ProblemDto.ProblemResponseDto;
import com.boj.guidance.dto.ProblemDto.ProblemsResponseDto;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.ProblemRepository;
import com.boj.guidance.service.ProblemService;
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.DjangoException;
import com.boj.guidance.util.exception.MemberException;
import com.boj.guidance.util.exception.ProblemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {

    @Value("${django.server.address}")
    private String ADDRESS;

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    // 문제 id로 검색하기
    @Cacheable(value = "problem", key = "#id")
    @Override
    public ProblemResponseDto searchProblemById(Integer problemId) {
        return returnProblemById(problemId);
    }

    // 알고리즘 이름으로 문제들 검색
    @Cacheable(value = "problem", key = "#name")
    @Override
    public ProblemsResponseDto searchAllProblemByAlgorithm(String name) {
        List<Integer> problemIds = problemRepository.findAllProblemIds(name);
        ArrayList<ProblemResponseDto> problems = new ArrayList<>();
        for (Integer problemId : problemIds) {
            problems.add(returnProblemById(problemId));
        }
        List<ProblemResponseDto> problemResponseDtos = problems.stream().toList();
        return new ProblemsResponseDto().toArray(problemResponseDtos, problemResponseDtos.size());
    }

    // 랜덤으로 5개의 문제를 뽑아 반환
    @Override
    public ProblemsResponseDto returnRandomProblems() {
        List<Problem> randomProblem = problemRepository.findRandomProblem();
        ArrayList<ProblemResponseDto> problems = new ArrayList<>();
        for(Problem problem : randomProblem) {
            problems.add(returnProblemById(problem.getProblemId()));
        }
        List<ProblemResponseDto> problemResponseDtos = problems.stream().toList();
        return new ProblemsResponseDto().toArray(problemResponseDtos, problemResponseDtos.size());
    }

    // model 연결하여 문제 추천
    @Override
    public ProblemsResponseDto recommendProblems(String handle) {
        Member member = memberRepository.findByHandle(handle).orElseThrow(
                () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
        );
        String apiUrl = ADDRESS + "/recommend/" + member.getHandle();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, List<Integer>> responseData = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, List<Integer>>>() {});
            List<Integer> recommend = responseData.get("recommend");
            ArrayList<ProblemResponseDto> problemList = new ArrayList<>();
            for (Integer problemId : recommend) {
                problemList.add(returnProblemById(problemId));
            }
            List<ProblemResponseDto> problemResponseDtos = problemList.stream().toList();
            return new ProblemsResponseDto().toArray(problemResponseDtos, problemResponseDtos.size());
        } catch (JsonProcessingException e) {
            throw new DjangoException(ResponseCode.PROBLEM_RECOMMEND_FAIL);
        }
    }

    // 문제 id로 문제 알고리즘과 함께 반환하여 주는 공통 기능 메서드
    public ProblemResponseDto returnProblemById(Integer problemId) {
        Optional<Problem> problemOptional = problemRepository.findById(problemId);
        if (problemOptional.isPresent()) {
            Problem problem = problemOptional.get();
            List<String> algorithms = problemRepository.findAlgorithmsById(problem.getProblemId());
            return new ProblemResponseDto().toResponse(problem, algorithms);
        } else {
            throw new ProblemException(ResponseCode.PROBLEM_FIND_FAIL);
        }
    }
}
