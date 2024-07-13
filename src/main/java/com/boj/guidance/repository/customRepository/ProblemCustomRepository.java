package com.boj.guidance.repository.customRepository;

import com.boj.guidance.domain.Problem;

import java.util.List;

public interface ProblemCustomRepository {
    List<Problem> findRandomProblem();
}
