package com.boj.guidance.domain;

import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CodeAnalysis {
    @Id
    @LockSerial
    private String id;
    private String submitId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String response;
    private String codeContent;
    private String problemId;
    private Boolean result;
    private String language;

    @Builder
    public CodeAnalysis(
            String submitId,
            Member member,
            String response,
            String codeContent,
            String problemId,
            Boolean result,
            String language
    ) {
        this.submitId = submitId;
        this.member = member;
        this.response = response;
        this.codeContent = codeContent;
        this.problemId = problemId;
        this.result = result;
        this.language = language;
    }

}
