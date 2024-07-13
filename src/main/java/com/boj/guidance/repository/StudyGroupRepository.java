package com.boj.guidance.repository;

import com.boj.guidance.domain.StudyGroup;
import com.boj.guidance.repository.customRepository.StudyGroupCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, String>, StudyGroupCustomRepository {
}
