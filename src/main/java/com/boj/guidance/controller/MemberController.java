package com.boj.guidance.controller;

import com.boj.guidance.dto.MemberDto.*;
import com.boj.guidance.service.MemberService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
        MemberResponseDto joined = memberService.join(dto);
        return ApiResponse.success(ResponseCode.MEMBER_JOIN_SUCCESS.getMessage(), joined);
    }

    /**
     * 사용자 로그인 기능
     */
    @PostMapping("/login")
    public ApiResponse<MemberResponseDto> login(@RequestBody MemberLoginRequestDto dto, final HttpServletRequest httpRequest) {
        MemberResponseDto login = memberService.login(dto, httpRequest);
        final HttpSession session = httpRequest.getSession();
        session.setAttribute("memberId", login.getHandle());
        return ApiResponse.success(ResponseCode.MEMBER_LOGIN_SUCCESS.getMessage(), login);
    }

    /**
     * 백준 사용자 인증하기
     */
    @PostMapping("/auth")
    public MemberAuthRequestDto authorize() {
        return memberService.authorize();
    }

    /**
     * 사용자 역할 변경
     */
    @PutMapping("/role/{id}")
    public ApiResponse<MemberResponseDto> changeRole(@PathVariable("id") String id) {
        return ApiResponse.success(ResponseCode.MEMBER_ROLE_CHANGE_SUCCESS.getMessage(), memberService.changeRole(id));
    }

    /**
     * 사용자 스터디그룹 모집 활성화 상태 변경
     */
    @PutMapping("/state/{id}")
    public ApiResponse<MemberResponseDto> changeState(@PathVariable("id") String id) {
        return ApiResponse.success(ResponseCode.MEMBER_STATE_CHANGE_SUCCESS.getMessage(), memberService.changeState(id));
    }

    /**
     * 사용자 취약 알고리즘 업데이트
     */
    @PutMapping("/weak/{id}")
    public ApiResponse<MemberResponseDto> updateWeakAlgorithm(@PathVariable("id") String id,
                                                              @RequestParam("weakAlgorithm") String weakAlgorithm) {
        return ApiResponse.success(ResponseCode.MEMBER_WEAK_ALGORITHM_UPDATE_SUCCESS.getMessage(), memberService.updateWeakAlgorithm(id, weakAlgorithm));
    }

    @GetMapping("/update")
    public void updateMemberDetails() {
        memberService.updateDetails();
    }

}
