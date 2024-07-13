package com.boj.guidance.repository.customRepository;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.StudyGroup;

import java.util.List;
import java.util.Optional;

public interface StudyGroupCustomRepository {
    Optional<StudyGroup> findByMemberId(String handle);

    List<StudyGroup> findAllByIsDeletedIsFalse();

    List<Member> findMemberToJoinStudyGroup(String mainAlgorithm, Long rating);
}
