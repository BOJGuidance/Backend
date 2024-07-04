package com.boj.guidance.domain;

import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @Id
    @LockSerial
    private String id;
    @Column(columnDefinition = "LONGTEXT")
    private String codeContent;             // 코드 내용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  // 제출자
    private String submitId;                // 제출 번호
    private String problemId;               // 문제 번호
    private Boolean result;                 // 결과 (성공/실패)
    private String memory;                  // 메모리
    private String time;                    // 컴파일 시간
    private String language;                // 작성 언어
    private String codeLength;              // 코드 길이

    @Builder
    public Submission(
            String codeContent,
            Member member,
            String submitId,
            String problemId,
            Boolean result,
            String memory,
            String time,
            String language,
            String codeLength
    ) {
        this.codeContent = codeContent;
        this.member = member;
        this.submitId = submitId;
        this.problemId = problemId;
        this.result = result;
        this.memory = memory;
        this.time = time;
        this.language = language;
        this.codeLength = codeLength;
    }
}
