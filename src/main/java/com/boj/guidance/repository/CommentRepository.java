package com.boj.guidance.repository;

import com.boj.guidance.domain.Comment;
import com.boj.guidance.repository.customRepository.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String>, CommentCustomRepository {
}
