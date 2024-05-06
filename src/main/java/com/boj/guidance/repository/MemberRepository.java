package com.boj.guidance.repository;

import com.boj.guidance.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);

    @Query(
            "SELECT m " +
            "FROM Member m " +
            "WHERE m.loginId = :id")
    Optional<Member> findByLoginId(@Param("id") String loginId);

    @Query(
            "SELECT m " +
            "FROM Member m " +
            "WHERE m.loginId = :id AND m.loginPassword = :password")
    Optional<Member> findMemberByLoginIdAndLoginPassword(@Param("id") String loginId, @Param("password") String loginPassword);

    @Modifying
    @Query(
            "UPDATE Member m " +
            "SET m.role = 'ADMIN' " +
            "WHERE m.id = :id")
    int updateRole(@Param("id") String id);

}
