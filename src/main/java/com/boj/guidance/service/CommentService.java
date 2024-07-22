package com.boj.guidance.service;

import com.boj.guidance.dto.CommentDto.CommentCreateRequestDto;
import com.boj.guidance.dto.CommentDto.CommentResponseDto;
import com.boj.guidance.dto.CommentDto.CommentsResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService {

    @Transactional
    CommentResponseDto createComment(String postId, CommentCreateRequestDto dto);

    @Transactional
    CommentResponseDto createChildComment(String postId, CommentCreateRequestDto dto);

    @Transactional
    CommentResponseDto deleteComment(String memberId, String commentId);

    @Transactional(readOnly = true)
    CommentsResponseDto commentsOfPost(String postId);
}
