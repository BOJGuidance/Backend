package com.boj.guidance.controller;

import com.boj.guidance.dto.MemberAuthRequestDto;
import com.boj.guidance.dto.MemberJoinRequestDto;
import com.boj.guidance.dto.MemberLoginRequestDto;
import com.boj.guidance.dto.MemberResponseDto;
import com.boj.guidance.service.MemberService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 사용자 회원가입 기능
     */
    @PostMapping("/join")
    public ApiResponse<MemberResponseDto> join(@RequestBody MemberJoinRequestDto dto) {
        log.info("회원가입 API 호출");
        MemberResponseDto joined = memberService.join(dto);

        return ApiResponse.success(ResponseCode.USER_JOIN_SUCCESS.getMessage(), joined);
    }

    /**
     * 사용자 로그인 기능
     */
    @PostMapping("/login")
    public ApiResponse<MemberResponseDto> login(@RequestBody MemberLoginRequestDto dto) {
        log.info("로그인 API 호출");
        MemberResponseDto login = memberService.login(dto);

        return ApiResponse.success(ResponseCode.USER_LOGIN_SUCCESS.getMessage(), login);
    }

    /**
     * 백준 사용자 인증하기
     */
    @PostMapping("/auth")
    public MemberAuthRequestDto authorize() {
        log.info("인증 API 호출");

        return memberService.authorize();
    }

}
