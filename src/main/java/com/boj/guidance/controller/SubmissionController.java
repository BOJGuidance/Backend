package com.boj.guidance.controller;

import com.boj.guidance.dto.CodeAnalysisDto.CodeAnalysisResponseDto;
import com.boj.guidance.dto.SubmissionDto.SubmissionReceiveRequestDto;
import com.boj.guidance.service.SubmissionService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/myapp")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})
public class SubmissionController {

    private final SubmissionService submissionService;

    /**
     * 사용자 백준에 문제 풀이 제출
     *
     * @param memberId 사용자 id
     * @param dto 문제 풀이 내용
     * @return 코드 분석 결과 정보
     */
    @PostMapping("/data/{memberId}")
    public ApiResponse<CodeAnalysisResponseDto> receiveSubmission(@PathVariable("memberId") String memberId,
                                                                  @RequestBody SubmissionReceiveRequestDto dto) {
        return ApiResponse.success(ResponseCode.CODE_ANALYSIS_SUCCESS.getMessage(), submissionService.saveSubmission(memberId, dto));
    }
}
