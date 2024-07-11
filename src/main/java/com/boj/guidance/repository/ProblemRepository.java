package com.boj.guidance.repository;

import com.boj.guidance.domain.Problem;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemRepository extends JpaRepository<Problem, Integer> {

    // 문제 id로 문제의 알고리즘 목록 탐색
    @Query(value =
            "SELECT name " +
            "FROM algorithm " +
            "WHERE problem_id = :id", nativeQuery = true)
    List<String> findAlgorithmsById(@Param("id") Integer id);

    // 특정 알고리즘을 포함하고 있는 문제 탐색
    @Query(value =
            "SELECT problem_id " +
            "FROM algorithm " +
            "WHERE name = :name", nativeQuery = true
    )
    List<Integer> findAllProblemIds(String name);

    // 취약 알고리즘을 포함하고 있는 문제 탐색
    @Query(value =
            "SELECT DISTINCT problem_id " +
            "FROM algorithm " +
            "WHERE name IN (:weakAlgorithms)", nativeQuery = true
    )
    List<Integer> findAllByWeakAlgorithms(@Param("weakAlgorithms") List<String> weakAlgorithms);

    // 랜덤으로 5개의 문제 뽑기
    @Query(value =
            "SELECT * " +
            "FROM problem " +
            "ORDER BY RAND() LIMIT 5", nativeQuery = true)
    List<Problem> findRandomProblem();

}
