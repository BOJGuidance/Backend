package com.boj.guidance.repository;

import com.boj.guidance.domain.SystemAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemAccessRepository extends JpaRepository<SystemAccess, String> {
}
