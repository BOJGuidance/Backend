package com.boj.guidance.dto.StudyGroupDto;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.StudyGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyGroupGenerateRequestDto {
    private String propose;     // 스터디그룹의 목표

    public StudyGroup toEntity(Member member) {
        return StudyGroup.builder()
                .member(member)
                .propose(propose)
                .build();
    }
}
