package com.boj.guidance.repository.customRepository.customRepositoryImpl;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.QStudyGroup;
import com.boj.guidance.domain.StudyGroup;
import com.boj.guidance.domain.enumerate.StudyGroupState;
import com.boj.guidance.repository.customRepository.StudyGroupCustomRepository;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.boj.guidance.domain.QMember.member;
import static com.boj.guidance.domain.QStudyGroup.studyGroup;

@Repository
@RequiredArgsConstructor
public class StudyGroupCustomRepositoryImpl implements StudyGroupCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<StudyGroup> findByMemberId(String handle) {
        QStudyGroup studyGroup = QStudyGroup.studyGroup;
        StudyGroup result = jpaQueryFactory
                .selectFrom(studyGroup)
                .where(member.handle.eq(handle))
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<StudyGroup> findAllByIsDeletedIsFalse() {
        return jpaQueryFactory
                .selectFrom(studyGroup)
                .where(studyGroup.isDeleted.eq(false))
                .fetch();
    }

    @Override
    public List<Member> findMemberToJoinStudyGroup(String mainAlgorithm, Long rating) {
        return jpaQueryFactory
                .selectFrom(member)
                .where(member.weakAlgorithm.contains(mainAlgorithm)
                        .and((member.rating.subtract(rating).abs()).loe(100L))
                        .and(member.state.eq(StudyGroupState.WAITING)))
                .orderBy(NumberExpression.random().asc())
                .limit(3)
                .fetch();
    }
}
