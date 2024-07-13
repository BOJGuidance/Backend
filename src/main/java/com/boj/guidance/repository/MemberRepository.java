package com.boj.guidance.repository;

import com.boj.guidance.domain.Member;
import com.boj.guidance.repository.customRepository.MemberCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String>, MemberCustomRepository {
}
