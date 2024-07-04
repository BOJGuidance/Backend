package com.boj.guidance.repository;

import com.boj.guidance.domain.CodeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeAnalysisRepository extends JpaRepository<CodeAnalysis, Long> {
}
