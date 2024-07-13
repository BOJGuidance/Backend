package com.boj.guidance.repository.customRepository.customRepositoryImpl;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.QMember;
import com.boj.guidance.repository.customRepository.MemberCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Member> findByHandle(String handle) {
        QMember member = QMember.member;
        Member result = jpaQueryFactory
                .selectFrom(member)
                .where(member.handle.eq(handle))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Member> findByLoginId(String loginId) {
        QMember member = QMember.member;
        Member result = jpaQueryFactory
                .selectFrom(member)
                .where(member.loginId.eq(loginId))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<Member> findMemberByLoginIdAndLoginPassword(String loginId, String loginPassword) {
        QMember member = QMember.member;
        Member result = jpaQueryFactory
                .selectFrom(member)
                .where(member.loginId.eq(loginId)
                        .and(member.loginPassword.eq(loginPassword)))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<String> findHandleById(String id) {
        QMember member = QMember.member;
        String result = jpaQueryFactory
                .select(member.handle).from(member)
                .where(member.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(result);
    }
}
