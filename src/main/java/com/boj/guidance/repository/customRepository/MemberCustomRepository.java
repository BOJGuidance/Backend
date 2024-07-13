package com.boj.guidance.repository.customRepository;

import com.boj.guidance.domain.Member;

import java.util.Optional;

public interface MemberCustomRepository {
    Optional<Member> findByHandle(String handle);

    Optional<Member> findByLoginId(String loginId);

    Optional<Member> findMemberByLoginIdAndLoginPassword(String loginId, String loginPassword);

    Optional<String> findHandleById(String id);
}
