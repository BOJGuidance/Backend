package com.boj.guidance.domain;

import com.boj.guidance.domain.enumerate.MemberRole;
import com.boj.guidance.domain.enumerate.StudyGroupState;
import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.*;
import lombok.*;
import org.aspectj.apache.bcel.classfile.Code;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {

    @Id
    @LockSerial
    private String id;
    private String createdAt;
    private String handle;                  // 사용자명
    private String loginId;                 // 로그인 id
    private String loginPassword;           // 로그인 password
    private String bio;                     // 자기소개
    private Long solvedCount;               // 푼 문제 수
    private Long tier;                      // 티어 (1-31)
    private Long rating;                    // 레이팅
    private Long ratingByProblemsSum;       // 푼 문제의 난이도 합으로 계산한 레이팅
    private Long ratingBySolvedCount;       // 푼 문제 수로 계산한 레이팅
    @Setter
    @Enumerated(EnumType.STRING)
    private MemberRole role;                // 사용자 역할
    @Setter
    @Enumerated(EnumType.STRING)
    private StudyGroupState state;          // 스터디그룹 매칭 활성화 상태
    @Setter
    private String weakAlgorithm;           // 취약 알고리즘
    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGroup studyGroup;          // 가입한 스터디그룹
    @OneToMany(mappedBy = "writer")
    private List<Post> postList = new ArrayList<>();
    @OneToMany(mappedBy = "writer")
    private List<Comment> commentList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Submission> submissionList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<CodeAnalysis> codeAnalysesList = new ArrayList<>();

    @Builder
    public Member(
            String handle,
            String loginId,
            String loginPassword,
            String bio,
            Long solvedCount,
            Long tier,
            Long rating,
            Long ratingByProblemsSum,
            Long ratingBySolvedCount
    ) {
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.handle = handle;
        this.loginId = loginId;
        this.loginPassword = loginPassword;
        this.bio = bio;
        this.solvedCount = solvedCount;
        this.tier = tier;
        this.rating = rating;
        this.ratingByProblemsSum = ratingByProblemsSum;
        this.ratingBySolvedCount = ratingBySolvedCount;
        this.role = MemberRole.USER;
        this.state = StudyGroupState.WAITING;
        this.weakAlgorithm = null;
        this.studyGroup = null;
    }

    public void stateUpdate() {
        if (state == StudyGroupState.WAITING) {
            setState(StudyGroupState.NOT_WAITING);
        } else {
            setState(StudyGroupState.WAITING);
        }
    }

    public void roleUpdate() {
        if (role == MemberRole.USER) {
            setRole(MemberRole.ADMIN);
        } else {
            setRole(MemberRole.USER);
        }
    }

    public void joinStudyGroup(StudyGroup studyGroup) {
        if (this.studyGroup != null) {
            this.studyGroup.removeMember(this);
        }
        stateUpdate();
        this.studyGroup = studyGroup;
        studyGroup.addMember(this);
    }

    public void exitStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = null;
        studyGroup.removeMember(this);
        stateUpdate();
    }

    public void addPost(Post post) {
        this.postList.add(post);
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void delComment(Comment comment) {
        this.commentList.remove(comment);
    }

    public void addSubmission(Submission submission) {
        this.submissionList.add(submission);
    }

    public void addCodeAnalysis(CodeAnalysis codeAnalysis) {
        this.codeAnalysesList.add(codeAnalysis);
    }

}
