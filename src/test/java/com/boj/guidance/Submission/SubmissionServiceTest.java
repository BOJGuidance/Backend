//package com.boj.guidance.Submission;
//
//import com.boj.guidance.domain.CodeAnalysis;
//import com.boj.guidance.domain.Member;
//import com.boj.guidance.domain.Submission;
//import com.boj.guidance.dto.CodeAnalysisDto.CodeAnalysisResponseDto;
//import com.boj.guidance.dto.SubmissionDto.SubmissionReceiveRequestDto;
//import com.boj.guidance.repository.CodeAnalysisRepository;
//import com.boj.guidance.repository.MemberRepository;
//import com.boj.guidance.repository.SubmissionRepository;
//import com.boj.guidance.service.SubmissionService;
//import com.boj.guidance.util.OpenAIClient;
//import com.boj.guidance.util.exception.CodeAnalysisException;
//import com.boj.guidance.util.exception.MemberException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import static com.boj.guidance.util.ObjectFixtures.*;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//
//@SpringBootTest
//public class SubmissionServiceTest {
//
//    @Autowired
//    SubmissionService submissionService;
//
//    @MockBean
//    MemberRepository memberRepository;
//
//    @MockBean
//    SubmissionRepository submissionRepository;
//
//    @MockBean
//    CodeAnalysisRepository codeAnalysisRepository;
//
//    @MockBean
//    OpenAIClient openAIClient;
//
//    @MockBean
//    ObjectMapper objectMapper;
//
//    @Configuration
//    static class TestConfig {
//        @Bean
//        public ObjectMapper objectMapper() {
//            return new ObjectMapper();
//        }
//    }
//
//    @Test
//    @DisplayName("백준 페이지에서 코드 제출 성공")
//    void saveSubmissionSuccess() throws IOException {
//        // given
//        Member member = getMember();
//        Submission submission = getSubmission();
//        SubmissionReceiveRequestDto receiveRequestDto = new SubmissionReceiveRequestDto("codeContent", "userName", "submitId", "problemId", "result", "memory", "time", "language", "codeLength");
//
//        String gptResponse = "{ \"choices\": [{ \"message\": { \"content\": \"{ \\\"time\\\": \\\"O(n)\\\", \\\"memory\\\": \\\"O(1)\\\", \\\"suggest\\\": \\\"Optimize the loop\\\" }\" } }] }";
//        JsonNode rootNode = objectMapper.readTree(gptResponse);
//        String analysisContent = rootNode.path("choices").get(0).path("message").path("content").asText();
//
//        given(memberRepository.findById("member")).willReturn(Optional.of(member));
//        given(submissionRepository.save(any(Submission.class))).willReturn(submission);
//        given(openAIClient.getGPTResponse(any(String.class))).willReturn(gptResponse);
//        given(objectMapper.readTree(gptResponse)).willReturn(rootNode);
//        given(codeAnalysisRepository.save(any(CodeAnalysis.class))).willReturn(CodeAnalysis.builder()
//                .submitId(submission.getSubmitId())
//                .member(member)
//                .response(analysisContent)
//                .codeContent(submission.getCodeContent())
//                .problemId(submission.getProblemId())
//                .language(submission.getLanguage())
//                .result(submission.getResult())
//                .build());
//
//        // when
//        CodeAnalysisResponseDto responseDto = submissionService.saveSubmission("member", receiveRequestDto);
//
//        // then
//        assertThat(responseDto).isNotNull();
//        assertThat(responseDto.getResponse()).isEqualTo(analysisContent);
//    }
//
//    @Test
//    @DisplayName("백준 페이지에서 코드 제출 시 회원 존재하지 않음")
//    void saveSubmissionMemberNotFound() {
//        // given
//        SubmissionReceiveRequestDto receiveRequestDto = new SubmissionReceiveRequestDto("codeContent", "userName", "submitId", "problemId", "result", "memory", "time", "language", "codeLength");
//
//        given(memberRepository.findById("member")).willReturn(Optional.empty());
//
//        // when / then
//        assertThrows(MemberException.class, () -> submissionService.saveSubmission("member", receiveRequestDto));
//    }
//
//    @Test
//    @DisplayName("백준 페이지에서 코드 제출 시 분석 실패")
//    void saveSubmissionAnalysisFail() throws IOException {
//        // given
//        Member member = getMember();
//        Submission submission = getSubmission();
//        SubmissionReceiveRequestDto receiveRequestDto = new SubmissionReceiveRequestDto("codeContent", "userName", "submitId", "problemId", "result", "memory", "time", "language", "codeLength");
//
//        given(memberRepository.findById("member")).willReturn(Optional.of(member));
//        given(submissionRepository.save(any(Submission.class))).willReturn(submission);
//        given(openAIClient.getGPTResponse(any(String.class))).willThrow(new IOException());
//
//        // when / then
//        assertThrows(CodeAnalysisException.class, () -> submissionService.saveSubmission("member", receiveRequestDto));
//    }
//}
