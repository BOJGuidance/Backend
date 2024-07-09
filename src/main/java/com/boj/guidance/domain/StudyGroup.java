package com.boj.guidance.domain;

import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class StudyGroup {
    @Id
    @LockSerial
    private String id;
    private String propose;             // 스터디그룹 공동 목표
    @OneToMany(mappedBy = "studyGroup")
    private List<Member> memberList;    // 스터디 부원 list
    @OneToMany
    private List<Problem> solvedList;   // 스터디 부원들이 함께 푼 문제 list
    @Setter
    private boolean isDeleted;          // 삭제 여부
    @Setter
    private Long avgRating;             // 부원들의 평균 rating
    private String mainAlgorithm;       // 스터디의 주요 알고리즘

    @Builder
    public StudyGroup(
            Member member,
            String propose
    ) {
        this.propose = propose;
        this.memberList = new ArrayList<>();
        this.solvedList = new ArrayList<>();
        this.isDeleted = false;
        this.avgRating = member.getRating();
        this.mainAlgorithm = Arrays.stream(member.getWeakAlgorithm().split(",")).toList().get(0);
    }

    public void addMember(Member member) {
        this.memberList.add(member);
    }

    public void removeMember(Member member) {
        this.memberList.remove(member);
    }

    public void addProblem(Problem problem) {
        this.solvedList.add(problem);
    }

    // 테스트용 id set 함수
    public void setId(String id) {
        this.id = id;
    }

}
