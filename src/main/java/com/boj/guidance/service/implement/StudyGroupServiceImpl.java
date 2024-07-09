package com.boj.guidance.service.implement;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Problem;
import com.boj.guidance.domain.StudyGroup;
import com.boj.guidance.dto.StudyGroupDto.StudyGroupGenerateRequestDto;
import com.boj.guidance.dto.StudyGroupDto.StudyGroupResponseDto;
import com.boj.guidance.dto.StudyGroupDto.StudyGroupsResponseDto;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.ProblemRepository;
import com.boj.guidance.repository.StudyGroupRepository;
import com.boj.guidance.service.StudyGroupService;
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.MemberException;
import com.boj.guidance.util.exception.ProblemException;
import com.boj.guidance.util.exception.StudyGroupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudyGroupServiceImpl implements StudyGroupService {

    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final StudyGroupRepository studyGroupRepository;

    // 모든 스터디그룹 목록 반환
    public StudyGroupsResponseDto findAllGroups() {
        List<StudyGroup> studyGroups = studyGroupRepository.findAllByIsDeletedIsFalse();
        List<StudyGroupResponseDto> groupList = new ArrayList<>();
        for (StudyGroup studyGroup : studyGroups) {
            groupList.add(new StudyGroupResponseDto().toResponse(studyGroup));
        }
        return new StudyGroupsResponseDto().toArray(groupList, groupList.size());
    }

    // 새로운 스터디그룹 생성
    @Override
    public StudyGroupResponseDto createGroup(String memberId, StudyGroupGenerateRequestDto dto) {
        Member member = getMember(memberId);
        StudyGroup saved = studyGroupRepository.save(dto.toEntity(member));
        member.joinStudyGroup(saved);
        return new StudyGroupResponseDto().toResponse(saved);
    }

    // 스터디그룹 삭제
    @Override
    public StudyGroupResponseDto deleteGroup(String groupId) {
        StudyGroup studyGroup = getStudyGroup(groupId);
        studyGroup.setDeleted(true);
        return new StudyGroupResponseDto().toResponse(studyGroupRepository.save(studyGroup));
    }

    // 스터디그룹에 사용자 입장
    @Override
    public void memberJoin(String memberId, StudyGroup studyGroup) {
        Member member = getMember(memberId);
        member.joinStudyGroup(studyGroup);
        memberRepository.save(member);
        studyGroup.setAvgRating((studyGroup.getAvgRating() + member.getRating()) / studyGroup.getMemberList().size());
        studyGroup.addMember(member);
        new StudyGroupResponseDto().toResponse(studyGroupRepository.save(studyGroup));
    }

    // 스터디그룹에 사용자 퇴장
    @Override
    public StudyGroupResponseDto memberExit(String memberId, String groupId) {
        Member member = getMember(memberId);
        StudyGroup studyGroup = getStudyGroup(groupId);
        member.exitStudyGroup(studyGroup);
        studyGroup.removeMember(member);
        memberRepository.save(member);
        return new StudyGroupResponseDto().toResponse(studyGroupRepository.save(studyGroup));
    }

    // 스터디그룹 구성원 검색/추가
    @Override
    public StudyGroupResponseDto recruitMember(String groupId) {
        StudyGroup studyGroup = getStudyGroup(groupId);
        List<Member> members = studyGroupRepository.findMemberToJoinStudyGroup(studyGroup.getMainAlgorithm(), studyGroup.getAvgRating());
        for (Member member : members) {
            memberJoin(member.getId(), studyGroup);
        }
        return new StudyGroupResponseDto().toResponse(studyGroupRepository.save(studyGroup));
    }

    // 스터디그룹 문제 풀이 추가
    @Override
    public StudyGroupResponseDto problemSolved(String groupId, Integer problemId) {
        Problem problem = getProblem(problemId);
        StudyGroup studyGroup = getStudyGroup(groupId);
        studyGroup.addProblem(problem);
        return new StudyGroupResponseDto().toResponse(studyGroupRepository.save(studyGroup));
    }

    @Override
    public Optional<StudyGroupResponseDto> checkIfMemberJoined(String handle) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findByMemberId(handle);
        if (studyGroup.isPresent()) {
            return studyGroup.map(group -> new StudyGroupResponseDto().toResponse(group));
        } else {
            return Optional.empty();
        }
    }

    private Member getMember(String memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
        );
    }

    private Problem getProblem(Integer problemId) {
        return problemRepository.findById(problemId).orElseThrow(
                () -> new ProblemException(ResponseCode.PROBLEM_FIND_FAIL)
        );
    }

    private StudyGroup getStudyGroup(String groupId) {
        return studyGroupRepository.findById(groupId).orElseThrow(
                () -> new StudyGroupException(ResponseCode.STUDY_GROUP_NOT_EXIST)
        );
    }
}
