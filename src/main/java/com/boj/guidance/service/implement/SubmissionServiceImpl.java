package com.boj.guidance.service.implement;

import com.boj.guidance.domain.CodeAnalysis;
import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Submission;
import com.boj.guidance.dto.CodeAnalysisDto.CodeAnalysisResponseDto;
import com.boj.guidance.dto.SubmissionDto.SubmissionReceiveRequestDto;
import com.boj.guidance.repository.CodeAnalysisRepository;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.SubmissionRepository;
import com.boj.guidance.service.SubmissionService;
import com.boj.guidance.util.OpenAIClient;
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.CodeAnalysisException;
import com.boj.guidance.util.exception.MemberException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

    private final MemberRepository memberRepository;
    private final SubmissionRepository submissionRepository;
    private final CodeAnalysisRepository codeAnalysisRepository;
    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    @Override
    public CodeAnalysisResponseDto saveSubmission(String memberId, SubmissionReceiveRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
                );
        Submission savedSubmission = submissionRepository.save(dto.toEntity(member));

        String prompt = "다음 코드를 분석하고 다음 형식으로 피드백을 제공해주세요:\n" +
                "## 출력 형식 \n" +
                "{\n" +
                "\"time\" : \"해당 알고리즘의 시간 복잡도는 O() 입니다. 시간 복잡도에 대한 상세한 설명\",\n" +
                "\"memory\" : \"해당 알고리즘의 공간 복잡도는 O() 입니다. 공간 복잡도에 대한 상세한 설명\",\n" +
                "\"suggest\" : \"개선 방안에 대한 상세한 피드백\"\n" +
                "}\n\n" +
                "코드:\n" + savedSubmission.getCodeContent();

        String gptResponse = null;
        JsonNode root = null;
        try {
            gptResponse = openAIClient.getGPTResponse(prompt);
            root = objectMapper.readTree(gptResponse);
        } catch (IOException e) {
            throw new CodeAnalysisException(ResponseCode.CODE_ANALYSIS_FAIL);
        }
        String content = root.path("choices").get(0).path("message").path("content").asText();

        CodeAnalysis codeAnalysis = CodeAnalysis.builder()
                .submitId(savedSubmission.getSubmitId())
                .member(member)
                .response(content)
                .codeContent(savedSubmission.getCodeContent())
                .problemId(savedSubmission.getProblemId())
                .language(savedSubmission.getLanguage())
                .result(savedSubmission.getResult())
                .build();
        codeAnalysis = codeAnalysisRepository.save(codeAnalysis);
        return new CodeAnalysisResponseDto().toDto(codeAnalysis);
    }

}
