package com.boj.guidance.repository.customRepository;

import com.boj.guidance.domain.Comment;

import java.util.List;

public interface CommentCustomRepository {
    List<Comment> findAllByPostId(String postId);
}
