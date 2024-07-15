package com.boj.guidance.controller;

import com.boj.guidance.dto.ProblemDto.ProblemResponseDto;
import com.boj.guidance.dto.ProblemDto.ProblemsResponseDto;
import com.boj.guidance.service.ProblemService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problem")
public class ProblemController {

    private final ProblemService problemService;

    /**
     * 문제 id로 문제 목록 조회
     *
     * @param problemId 문제 id
     * @return 문제 정보
     */
    @GetMapping("/id")
    public ApiResponse<ProblemResponseDto> getProblemsById(@RequestParam Integer problemId) {
        return ApiResponse.success(ResponseCode.ALGORITHM_NAME_SEARCH_SUCCESS.getMessage(), problemService.searchProblemById(problemId));
    }

    /**
     * 알고리즘 이름으로 문제 목록 조회
     *
     * @param name 알고리즘 이름
     * @return 문제 목록
     */
    @GetMapping("/algorithm")
    public ApiResponse<ProblemsResponseDto> getProblemsByAlgorithmName(@RequestParam String name) {
        return ApiResponse.success(ResponseCode.ALGORITHM_NAME_SEARCH_SUCCESS.getMessage(), problemService.searchAllProblemByAlgorithm(name));
    }

    /**
     * 문제 추천
     *
     * @param memberId 추천 받는 사용자 id
     * @return 추천된 문제 목록
     */
    @GetMapping("/recommend/{memberId}")
    public ApiResponse<ProblemsResponseDto> getProblemsByRecommend(@PathVariable("memberId") String memberId) {
        return ApiResponse.success(ResponseCode.PROBLEM_RECOMMEND_SUCCESS.getMessage(), problemService.recommendation(memberId));
    }

}
