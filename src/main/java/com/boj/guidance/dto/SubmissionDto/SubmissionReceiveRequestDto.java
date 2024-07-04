package com.boj.guidance.dto.SubmissionDto;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Submission;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SubmissionReceiveRequestDto {
    private String codeContent;
    private String userName;
    private String submitId;
    private String problemId;
    private String result;
    private String memory;
    private String time;
    private String language;
    private String codeLength;

    public Submission toEntity(Member member){
        return Submission.builder()
                .codeContent(codeContent)
                .member(member)
                .submitId(submitId)
                .problemId(problemId)
                .result(result.equals("맞았습니다!!"))
                .memory(memory)
                .time(time)
                .language(language)
                .codeLength(codeLength)
                .build();
    }
}

