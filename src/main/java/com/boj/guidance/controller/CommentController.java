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
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 생성
     *
     * @param memberId 사용자 id
     * @param postId 게시글 id
     * @param dto 댓글 내용
     * @return 저장된 댓글 정보
     */
    @PostMapping("/{postId}/{memberId}")
    public ApiResponse<CommentResponseDto> create(
            @PathVariable("memberId") String memberId,
            @PathVariable("postId") String postId,
            @RequestBody CommentCreateRequestDto dto) {
        return ApiResponse.success(ResponseCode.COMMENT_CREATE_SUCCESS.getMessage(), commentService.createComment(memberId, postId, dto));
    }

    /**
     * 대댓글 생성
     *
     * @param memberId 사용자 id
     * @param postId 게시글 id
     * @param commentId 부모 댓글 id
     * @param dto 댓글 내용
     * @return 저장된 대댓글 정보
     */
    @PostMapping("/{postId}/{memberId}/{commentId}")
    public ApiResponse<CommentResponseDto> create(
            @PathVariable("memberId") String memberId,
            @PathVariable("postId") String postId,
            @PathVariable("commentId") String commentId,
            @RequestBody CommentCreateRequestDto dto) {
        return ApiResponse.success(ResponseCode.COMMENT_CREATE_SUCCESS.getMessage(), commentService.createChildComment(memberId, postId, commentId, dto));
    }

    /**
     * 댓글 삭제
     *
     * @param memberId 삭제 사용자 id
     * @param commentId 삭제 댓글 id
     * @return 삭제된 댓글 정보
     */
    @PatchMapping("/{commentId}/{memberId}")
    public ApiResponse<CommentResponseDto> delete(
            @PathVariable("memberId") String memberId,
            @PathVariable("commentId") String commentId) {
        return ApiResponse.success(ResponseCode.COMMENT_DELETE_SUCCESS.getMessage(), commentService.deleteComment(memberId, commentId));
    }

    /**
     * 게시물의 모든 댓글 목록 조회
     *
     * @param postId 조회하려는 게시물 id
     * @return 댓글 목록
     */
    @GetMapping("/{postId}")
    public ApiResponse<CommentsResponseDto> findAllByPost(@PathVariable("postId") String postId) {
        return ApiResponse.success(ResponseCode.COMMENT_RESPONSE_SUCCESS.getMessage(), commentService.commentsOfPost(postId));
    }

}
