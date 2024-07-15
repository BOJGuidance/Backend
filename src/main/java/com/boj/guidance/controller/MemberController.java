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
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     *
     * @param dto 사용자 정보
     * @return 회원가입된 사용자 정보
     */
    @PostMapping("/join")
    public ApiResponse<MemberResponseDto> join(@RequestBody MemberJoinRequestDto dto) {
        MemberResponseDto joined = memberService.join(dto);
        return ApiResponse.success(ResponseCode.MEMBER_JOIN_SUCCESS.getMessage(), joined);
    }

    /**
     * 로그인
     *
     * @param dto id/pw
     * @param httpRequest 요청 header
     * @return 로그인한 사용자 정보
     */
    @GetMapping("/login")
    public ApiResponse<MemberResponseDto> login(@RequestBody MemberLoginRequestDto dto, final HttpServletRequest httpRequest) {
        MemberResponseDto login = memberService.login(dto, httpRequest);
        final HttpSession session = httpRequest.getSession();
        session.setAttribute("memberId", login.getHandle());
        return ApiResponse.success(ResponseCode.MEMBER_LOGIN_SUCCESS.getMessage(), login);
    }

    /**
     * 백준 사용자 정보 인증
     *
     * @return 사용자 정보
     */
    @GetMapping("/auth")
    public MemberAuthRequestDto authorize() {
        return memberService.authorize();
    }

    /**
     * 사용자 역할 변경
     *
     * @param memberId 사용자 id
     * @return 변경된 사용자 정보
     */
    @PutMapping("/role/{memberId}")
    public ApiResponse<MemberResponseDto> changeRole(@PathVariable("memberId") String memberId) {
        return ApiResponse.success(ResponseCode.MEMBER_ROLE_CHANGE_SUCCESS.getMessage(), memberService.changeRole(memberId));
    }

    /**
     * 스터디그룹 대기 상태 변경
     *
     * @param memberId 사용자 id
     * @return 변경된 사용자 정보
     */
    @PutMapping("/state/{memberId}")
    public ApiResponse<MemberResponseDto> changeState(@PathVariable("memberId") String memberId) {
        return ApiResponse.success(ResponseCode.MEMBER_STATE_CHANGE_SUCCESS.getMessage(), memberService.changeState(memberId));
    }

    /**
     * 취약 알고리즘 변경
     *
     * @param memberId 사용자 id
     * @param weakAlgorithm 취약 알고리즘
     * @return 변경된 사용자 정보
     */
    @PutMapping("/weak/{memberId}")
    public ApiResponse<MemberResponseDto> updateWeakAlgorithm(@PathVariable("memberId") String memberId,
                                                              @RequestParam("weakAlgorithm") String weakAlgorithm) {
        return ApiResponse.success(ResponseCode.MEMBER_WEAK_ALGORITHM_UPDATE_SUCCESS.getMessage(), memberService.updateWeakAlgorithm(memberId, weakAlgorithm));
    }

    /**
     * 사용자 정보 일괄 수정 (취약 알고리즘)
     */
    @PutMapping("/update")
    public void updateMemberDetails() {
        memberService.updateDetails();
    }

}
