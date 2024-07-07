package com.boj.guidance.StudyGroup;

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
import com.boj.guidance.util.ObjectFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class StudyGroupServiceTest {

    @Autowired
    StudyGroupService studyGroupService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    ProblemRepository problemRepository;

    @MockBean
    StudyGroupRepository studyGroupRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("스터디그룹 생성")
    void createGroup() {
        // given
        StudyGroupGenerateRequestDto generateRequestDto = new StudyGroupGenerateRequestDto("test");
        Member member = ObjectFixtures.getMember();
        StudyGroup studyGroup = new StudyGroup(member, generateRequestDto.getPropose());

        given(memberRepository.findById("member")).willReturn(Optional.of(member));
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        StudyGroupResponseDto created = studyGroupService.createGroup("member", generateRequestDto);

        // then
        assertEquals(1, created.getMemberList().size());
        assertEquals("test", created.getPropose());
        assertEquals("algorithm", created.getMainAlgorithm());
        verify(studyGroupRepository, times(1)).save(any(StudyGroup.class));
    }

    @Test
    @DisplayName("스터디그룹 조회")
    void findAllGroup() {
        // given
        Member member = ObjectFixtures.getMember();
        StudyGroup studyGroup1 = new StudyGroup(member, "studyGroup1");
        StudyGroup studyGroup2 = new StudyGroup(member, "studyGroup2");
        List<StudyGroup> list = new ArrayList<>();
        list.add(studyGroup1);
        list.add(studyGroup2);

        given(studyGroupRepository.findAllByIsDeletedIsFalse()).willReturn(list);

        // when
        StudyGroupsResponseDto found = studyGroupService.findAllGroups();

        // then
        assertEquals(2, found.getCount());
        assertNotNull(found);
        assertEquals(2, found.getStudyGroupList().size());
        assertEquals("studyGroup1", found.getStudyGroupList().get(0).getPropose());
        assertEquals("studyGroup2", found.getStudyGroupList().get(1).getPropose());

    }

    @Test
    @DisplayName("스터디그룹 삭제")
    void deleteGroup() {
        // given
        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();

        given(studyGroupRepository.findById("studyGroup")).willReturn(Optional.of(studyGroup));
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        StudyGroupResponseDto deleted = studyGroupService.deleteGroup("studyGroup");

        // then
        assertTrue(deleted.isDeleted());
        verify(studyGroupRepository, times(1)).save(any(StudyGroup.class));
    }

    @Test
    @DisplayName("스터디그룹 가입")
    void joinGroup() {
        // given
        Member member = ObjectFixtures.getMember();
        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();

        given(memberRepository.findById("user1")).willReturn(Optional.of(member));
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        studyGroupService.memberJoin("user1", studyGroup);

        // then
        assertEquals(2, studyGroup.getMemberList().size());
        assertEquals("studyGroup1", member.getStudyGroup().getPropose());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(studyGroupRepository, times(1)).save(any(StudyGroup.class));
    }

    @Test
    @DisplayName("스터디그룹 탈퇴")
    void exitGroup() {
        // given
        Member member = ObjectFixtures.getMember();
        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();

        given(memberRepository.findById("member")).willReturn(Optional.of(member));
        given(studyGroupRepository.findById("studyGroup")).willReturn(Optional.of(studyGroup));
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        StudyGroupResponseDto eliminated = studyGroupService.memberExit("member", "studyGroup");

        // then
        assertEquals(0, eliminated.getMemberList().size());
        assertNull(member.getStudyGroup());
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(studyGroupRepository, times(1)).save(any(StudyGroup.class));
    }

    @Test
    @DisplayName("스터디그룹 모집")
    void recruitGroup() { // 보류
//        // given
//        Member member1 = new Member("user1", "user1", "test", "test", 1L, 1L, 1L, 1L, 1L); member1.setWeakAlgorithm("algorithm");
//        Member member2 = new Member("user2", "user2", "test", "test", 2L, 2L, 2L, 2L, 2L); member2.setWeakAlgorithm("algorithm");
//        Member member3 = new Member("user3", "user3", "test", "test", 3L, 3L, 3L, 3L, 3L); member3.setWeakAlgorithm("notSelected");
//        Member member4 = new Member("user4", "user4", "test", "test", 4L, 4L, 4L, 4L, 4L); member4.setWeakAlgorithm("algorithm");
//        Member member5 = new Member("user5", "user5", "test", "test", 5L, 5L, 5L, 5L, 5L); member5.setWeakAlgorithm("algorithm");
//        Member member6 = new Member("user6", "user6", "test", "test", 6L, 6L, 6L, 6L, 6L); member6.setWeakAlgorithm("notSelected");
//        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();
//        studyGroup.setAvgRating(4L);
//        List<Member> joined = Arrays.asList(member1, member2, member4, member5);
//
//        given(memberRepository.findById(""))
//        given(studyGroupRepository.findById("studyGroup")).willReturn(Optional.of(studyGroup));
//        given(studyGroupRepository.findMemberToJoinStudyGroup(studyGroup.getMainAlgorithm(), studyGroup.getAvgRating())).willReturn(joined);
//
//        // when
//        StudyGroupResponseDto recruited = studyGroupService.recruitMember("studyGroup");
//
//        // then
//        assertEquals(4, recruited.getMemberList().size());
//        assertEquals("user1", recruited.getMemberList().get(0).getHandle());
//        assertEquals("user2", recruited.getMemberList().get(1).getHandle());
//        assertEquals("user4", recruited.getMemberList().get(3).getHandle());
//        assertEquals("user5", recruited.getMemberList().get(4).getHandle());
//        verify(memberRepository, times(4)).save(any(Member.class));
//        verify(studyGroupRepository, times(1)).save(any(StudyGroup.class));
    }

    @Test
    @DisplayName("스티디그룹 내의 문제 풀이")
    void problemSolvedInGroup() {
        // given
        Problem problem = ObjectFixtures.getProblem();
        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();

        given(problemRepository.findById(1000)).willReturn(Optional.of(problem));
        given(studyGroupRepository.findById("studyGroup")).willReturn(Optional.of(studyGroup));
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        StudyGroupResponseDto solved = studyGroupService.problemSolved("studyGroup", 1000);

        // then
        assertEquals(1, solved.getSolvedList().size());
        assertEquals(1000, solved.getSolvedList().get(0).getProblemId());
        verify(studyGroupRepository, times(1)).save(any(StudyGroup.class));
    }

    @Test
    @DisplayName("사용자가 스터디그룹에 가입되어 있는 상태")
    void checkMemberJoined() {
        // given
        Member member = ObjectFixtures.getMember();
        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();
        studyGroup.addMember(member);

        given(studyGroupRepository.findByMemberId("member")).willReturn(Optional.of(studyGroup));
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        Optional<StudyGroupResponseDto> found = studyGroupService.checkIfMemberJoined("member");

        // then
        assertTrue(found.isPresent());
        assertEquals(1, studyGroup.getMemberList().size());
    }

    @Test
    @DisplayName("사용자가 스터디그룹에 가입되어 있지 않은 상태")
    void checkNotMemberJoined() {
        // given
        Member member = ObjectFixtures.getMember();
        StudyGroup studyGroup = ObjectFixtures.getStudyGroup();

        given(studyGroupRepository.findByMemberId("member")).willReturn(Optional.empty());
        given(studyGroupRepository.save(any(StudyGroup.class))).willReturn(studyGroup);

        // when
        Optional<StudyGroupResponseDto> found = studyGroupService.checkIfMemberJoined("member");

        // then
        assertFalse(found.isPresent());
        assertEquals(0, studyGroup.getMemberList().size());
    }

}
