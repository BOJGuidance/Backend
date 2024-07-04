package com.boj.guidance.service;

import com.boj.guidance.dto.CodeAnalysisDto.CodeAnalysisResponseDto;
import com.boj.guidance.dto.SubmissionDto.SubmissionReceiveRequestDto;
import org.springframework.transaction.annotation.Transactional;

public interface SubmissionService {
    @Transactional
    CodeAnalysisResponseDto saveSubmission(String memberId, SubmissionReceiveRequestDto dto);
}
