package com.boj.guidance.Member;

import com.boj.guidance.config.PasswordEncoder;
import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.enumerate.MemberRole;
import com.boj.guidance.domain.enumerate.StudyGroupState;
import com.boj.guidance.dto.MemberDto.MemberJoinRequestDto;
import com.boj.guidance.dto.MemberDto.MemberLoginRequestDto;
import com.boj.guidance.dto.MemberDto.MemberResponseDto;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.service.MemberService;
import com.boj.guidance.util.ObjectFixtures;
import com.boj.guidance.util.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void joinMember() {
        // given
        MemberJoinRequestDto joinRequestDto = new MemberJoinRequestDto("newUser", "id", "pw", "bio", 1L, 1L, 1L, 1L, 1L);
        Member member = new Member(joinRequestDto.getHandle(), joinRequestDto.getLoginId(), joinRequestDto.getLoginPassword(), joinRequestDto.getBio(), joinRequestDto.getSolvedCount(), joinRequestDto.getTier(), joinRequestDto.getRating(), joinRequestDto.getRatingByProblemsSum(), joinRequestDto.getRatingBySolvedCount());
        given(memberRepository.existsById("test")).willReturn(false);
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberResponseDto created = memberService.join(joinRequestDto);

        // then
        assertEquals("newUser", created.getHandle());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 아이디")
    void duplicatedId() {
        // given
        MemberJoinRequestDto joinRequestDto = new MemberJoinRequestDto("newUser", "id", "pw", "bio", 1L, 1L, 1L, 1L, 1L);
        Member member = new Member(joinRequestDto.getHandle(), joinRequestDto.getLoginId(), joinRequestDto.getLoginPassword(), joinRequestDto.getBio(), joinRequestDto.getSolvedCount(), joinRequestDto.getTier(), joinRequestDto.getRating(), joinRequestDto.getRatingByProblemsSum(), joinRequestDto.getRatingBySolvedCount());
        given(memberRepository.findByLoginId(joinRequestDto.getLoginId())).willReturn(Optional.of(member));

        // when
        assertThrows(MemberException.class, () -> memberService.join(joinRequestDto));
    }

    @Test
    @DisplayName("로그인")
    void loginMember() {
        // given
        Member member = ObjectFixtures.getMember();
        MemberLoginRequestDto loginRequestDto = new MemberLoginRequestDto("user1", "test");

        given(memberRepository.existsById("user1")).willReturn(true);
        given(memberRepository.findMemberByLoginIdAndLoginPassword("user1", passwordEncoder.encrypt("test"))).willReturn(Optional.of(member));

        // when
        MemberResponseDto login = memberService.login(loginRequestDto);

        // then
        assertEquals("user1", login.getHandle());
        assertEquals("test", login.getBio());
        assertEquals(1L, login.getSolvedCount());
    }

    @Test
    @DisplayName("역할 변경")
    void changeRole() {
        // given
        Member member = ObjectFixtures.getMember();

        given(memberRepository.findById("test")).willReturn(Optional.of(member));
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberResponseDto changed = memberService.changeRole("test");

        // then
        assertEquals(MemberRole.ADMIN.toString(), changed.getRole());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("상태 변경")
    void changeState() {
        // given
        Member member = ObjectFixtures.getMember();

        given(memberRepository.findById("test")).willReturn(Optional.of(member));
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberResponseDto changed = memberService.changeState("test");

        // then
        assertEquals(StudyGroupState.NOT_WAITING, changed.getState());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("취약 알고리즘 수정")
    void updateWeakAlgorithm() {
        // given
        Member member = ObjectFixtures.getMember();
        String newAlgorithm = "newAlgorithm";

        given(memberRepository.findByHandle("test")).willReturn(Optional.of(member));
        given(memberRepository.save(any(Member.class))).willReturn(member);

        // when
        MemberResponseDto updated = memberService.updateWeakAlgorithm("test", newAlgorithm);

        // then
        assertEquals("newAlgorithm", updated.getWeakAlgorithms().get(0));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

}
