package com.boj.guidance.dto.CodeAnalysisDto;

import com.boj.guidance.domain.CodeAnalysis;
import com.boj.guidance.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CodeAnalysisResponseDto {
    private String id;
    private String submitId;
    private String member;
    private String response;
    private String codeContent;
    private String problemId;
    private Boolean result;
    private String language;

    @Builder
    public CodeAnalysisResponseDto(
            String id,
            String submitId,
            Member member,
            String response,
            String codeContent,
            String problemId,
            Boolean result,
            String language
    ) {
        this.id = id;
        this.submitId = submitId;
        this.member = member.getHandle();
        this.response = response;
        this.codeContent = codeContent;
        this.problemId = problemId;
        this.result = result;
        this.language = language;
    }

    public CodeAnalysisResponseDto toDto(CodeAnalysis entity) {
        return CodeAnalysisResponseDto.builder()
                .id(entity.getId())
                .submitId(entity.getSubmitId())
                .member(entity.getMember())
                .response(entity.getResponse())
                .codeContent(entity.getCodeContent())
                .problemId(entity.getProblemId())
                .result(entity.getResult())
                .language(entity.getLanguage())
                .build();
    }
}
