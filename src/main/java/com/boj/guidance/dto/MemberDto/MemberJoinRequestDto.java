package com.boj.guidance.dto.MemberDto;

import com.boj.guidance.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberJoinRequestDto {
    private String handle;               // 사용자명
    private String loginId;              // 로그인 id
    private String loginPassword;        // 로그인 pw
    private String bio;                  // 자기소개
    private Long solvedCount;            // 푼 문제 수
    private Long tier;                   // 티어 (1-31)
    private Long rating;                 // 레이팅
    private Long ratingByProblemsSum;    // 푼 문제의 난이도 합으로 계산한 레이팅
    private Long ratingBySolvedCount;    // 푼 문제 수로 계산한 레이팅

    public Member toEntity(String encoded) {
        return Member.builder()
                .handle(handle)
                .loginId(loginId)
                .loginPassword(encoded)
                .bio(bio)
                .solvedCount(solvedCount)
                .tier(tier)
                .rating(rating)
                .ratingByProblemsSum(ratingByProblemsSum)
                .ratingBySolvedCount(ratingBySolvedCount)
                .build();
    }

}
