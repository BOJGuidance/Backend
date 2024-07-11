package com.boj.guidance.controller.page;

import com.boj.guidance.domain.CodeAnalysis;
import com.boj.guidance.repository.CodeAnalysisRepository;
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.CodeAnalysisException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import com.boj.guidance.dto.MemberDto.WeakAlgorithmRequestDto;
import com.boj.guidance.dto.StudyGroupDto.StudyGroupResponseDto;
import com.boj.guidance.service.MemberService;
import com.boj.guidance.service.StudyGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PageController {

    private final MemberService memberService;
    private final StudyGroupService studyGroupService;

    private final CodeAnalysisRepository codeAnalysisRepository;
    private final ObjectMapper objectMapper;

    // 로그인 페이지 이동
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원가입 페이지 이동
    @GetMapping("/join")
    public String join() {
        return "join";
    }

    // 홈페이지 이동
    @GetMapping(value = {"/home", "/"})
    public String home(HttpSession session) {
        String userName = (String) session.getAttribute("memberId");
        return "home";
    }

    // 스터디그룹 페이지
    @GetMapping("/study")
    public String study(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("memberId");
        Optional<StudyGroupResponseDto> dto = studyGroupService.checkIfMemberJoined(userName);
        if (dto.isPresent()) {
            model.addAttribute("group", dto.get());
        } else {
            model.addAttribute("group", null);
        }
        if (userName == null) {
            return "redirect:/login";
        }
        return "study";
    }

    // 문제 추천 페이지
    @GetMapping("/recommendation")
    public String recommendation(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("memberId");
        if (userName != null) {
            model.addAttribute("handle", userName);
            return "recommendation";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/codeAnalysis")
    public String getCodeAnalysisList(HttpSession session,
                                      @RequestParam(required = false) String category,
                                      @RequestParam(required = false) String keyword,
                                      Model model) {
        String userName = (String) session.getAttribute("memberId");

        if (userName != null) {
            List<CodeAnalysis> responses;
            if (category != null && keyword != null && !keyword.isEmpty()) {
                switch (category) {
                    case "submitId":
                        responses = codeAnalysisRepository.findByUserNameAndSubmitIdContaining(userName, keyword);
                        break;
                    case "problemId":
                        responses = codeAnalysisRepository.findByUserNameAndProblemIdContaining(userName, keyword);
                        break;
                    case "problemTitle":
                        responses = codeAnalysisRepository.findByUserNameAndProblemTitleContaining(userName, keyword);
                        break;
                    default:
                        responses = codeAnalysisRepository.findByUserName(userName);
                }
            } else {
                responses = codeAnalysisRepository.findByUserName(userName);
            }
            model.addAttribute("responses", responses);
            model.addAttribute("username", userName);
            return "codeAnalysis";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/codeAnalysis/{id}")
    public String getCodeAnalysisDetail(HttpSession session,
                                        @PathVariable String id,
                                        Model model) {
        String userName = (String) session.getAttribute("memberId");
        CodeAnalysis response = codeAnalysisRepository.findById(id).orElseThrow(
                () -> new CodeAnalysisException(ResponseCode.CODE_ANALYSIS_FAIL)
        );

        try {
            JsonNode root = objectMapper.readTree(response.getResponse());
            String time = root.path("time").asText();
            String memory = root.path("memory").asText();
            String suggest = root.path("suggest").asText();

            model.addAttribute("time", time);
            model.addAttribute("memory", memory);
            model.addAttribute("suggest", suggest);
        } catch (IOException e) {
            throw new CodeAnalysisException(ResponseCode.CODE_ANALYSIS_FAIL, e);
        }

        model.addAttribute("response", response);
        model.addAttribute("username", userName);
        return "codeAnalysisDetail";
    }

    // 커뮤니티 페이지
    @GetMapping("/community")
    public String community(HttpSession session) {
        String userName = (String) session.getAttribute("memberId");
        if (userName != null) {
            return "community";
        } else {
            return "redirect:/login";
        }
    }

    // 나는 어디쯤일까? 페이지
    @GetMapping("/mypage")
    public String mypage(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("memberId");
//        WeakAlgorithmRequestDto dto = memberService.init(userName);
//        model.addAttribute("image", dto.getImage());
//        model.addAttribute("weak", dto.getWeak());
        return "mypage";
    }

    // 라이브 코딩 에디터
    @GetMapping("/editor")
    public String editor(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("memberId");
        if (studyGroupService.checkIfMemberJoined(userName).isPresent()) {
            return "editor";
        }
        return null;
    }
}
