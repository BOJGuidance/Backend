package com.boj.guidance.controller;

import com.boj.guidance.dto.CommentDto.CommentCreateRequestDto;
import com.boj.guidance.dto.CommentDto.CommentResponseDto;
import com.boj.guidance.dto.CommentDto.CommentsResponseDto;
import com.boj.guidance.service.CommentService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ApiResponse<CommentResponseDto> createComment (
            @PathVariable("postId") String postId,
            @RequestBody CommentCreateRequestDto dto
    ) {
        return ApiResponse.success(ResponseCode.COMMENT_CREATE_SUCCESS.getMessage(), commentService.createComment(postId, dto));
    }

    @PostMapping("/child/{postId}")
    public ApiResponse<CommentResponseDto> createChildComment(
            @PathVariable("postId") String postId,
            @RequestBody CommentCreateRequestDto dto
    ) {
        return ApiResponse.success(ResponseCode.COMMENT_CREATE_SUCCESS.getMessage(), commentService.createChildComment(postId, dto));
    }

    @PostMapping("/{commentId}/{memberId}")
    public ApiResponse<CommentResponseDto> delete(
            @PathVariable("memberId") String memberId,
            @PathVariable("commentId") String commentId
    ) {
        return ApiResponse.success(ResponseCode.COMMENT_DELETE_SUCCESS.getMessage(), commentService.deleteComment(memberId, commentId));
    }

    @GetMapping("/{postId}")
    public ApiResponse<CommentsResponseDto> findAllByPost(@PathVariable("postId") String postId) {
        return ApiResponse.success(ResponseCode.COMMENT_RESPONSE_SUCCESS.getMessage(), commentService.commentsOfPost(postId));
    }

}
