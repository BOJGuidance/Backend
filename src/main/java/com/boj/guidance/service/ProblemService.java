package com.boj.guidance.service;

import com.boj.guidance.dto.ProblemDto.ProblemResponseDto;
import com.boj.guidance.dto.ProblemDto.ProblemsResponseDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProblemService {

    @Transactional(readOnly = true)
    ProblemResponseDto searchProblemById(Integer problemId);

    @Transactional(readOnly = true)
    ProblemsResponseDto searchAllProblemByAlgorithm(String name);

}
