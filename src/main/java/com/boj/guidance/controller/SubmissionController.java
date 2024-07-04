package com.boj.guidance.controller;

import com.boj.guidance.dto.CodeAnalysisDto.CodeAnalysisResponseDto;
import com.boj.guidance.dto.SubmissionDto.SubmissionReceiveRequestDto;
import com.boj.guidance.service.SubmissionService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/myapp")
@RequiredArgsConstructor
@CrossOrigin(origins = {"*"})
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/data/{memberId}")
    public ApiResponse<CodeAnalysisResponseDto> receiveSubmission(@PathVariable("memberId") String memberId,
                                                                  @RequestBody SubmissionReceiveRequestDto dto) {
        return ApiResponse.success(ResponseCode.CODE_ANALYSIS_SUCCESS.getMessage(), submissionService.saveSubmission(memberId, dto));
    }
}
