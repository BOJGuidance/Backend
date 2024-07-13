package com.boj.guidance.repository.customRepository.customRepositoryImpl;

import com.boj.guidance.domain.Problem;
import com.boj.guidance.repository.customRepository.ProblemCustomRepository;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.boj.guidance.domain.QProblem.problem;

@Repository
@RequiredArgsConstructor
public class ProblemCustomRepositoryImpl implements ProblemCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Problem> findRandomProblem() {
        return jpaQueryFactory
                .selectFrom(problem)
                .orderBy(NumberExpression.random().asc())
                .limit(5)
                .fetch();
    }
}
