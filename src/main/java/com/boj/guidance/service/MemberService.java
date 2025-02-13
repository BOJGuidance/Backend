package com.boj.guidance.service;

import com.boj.guidance.dto.MemberDto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {

    @Transactional
    MemberResponseDto join(MemberJoinRequestDto dto);

    @Transactional
    MemberResponseDto login(MemberLoginRequestDto dto, HttpServletRequest httpRequest);

    MemberAuthRequestDto authorize();

    @Transactional
    MemberResponseDto changeRole(String memberId);

    @Transactional
    MemberResponseDto changeState(String memberId);

    @Transactional
    MemberResponseDto updateWeakAlgorithm(String handle, String algorithm);

    @Transactional
    void updateDetails();
}
