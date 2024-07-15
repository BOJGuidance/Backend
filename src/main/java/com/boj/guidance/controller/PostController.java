package com.boj.guidance.controller;

import com.boj.guidance.dto.PostDto.PostCreateRequestDto;
import com.boj.guidance.dto.PostDto.PostResponseDto;
import com.boj.guidance.service.PostService;
import com.boj.guidance.util.api.ApiResponse;
import com.boj.guidance.util.api.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 게시물 생성
     *
     * @param memberId 생성 사용자 id
     * @param dto 게시물 제목/내용
     * @return 생성된 게시물 정보
     */
    @PostMapping("/{memberId}")
    public ApiResponse<PostResponseDto> create(
            @PathVariable("memberId") String memberId,
            @RequestBody PostCreateRequestDto dto) {
        return ApiResponse.success(ResponseCode.POST_CREATE_SUCCESS.getMessage(), postService.createPost(memberId, dto));
    }

    /**
     * 게시물 좋아요
     *
     * @param postId 게시물 id
     */
    @GetMapping("/likes/{postId}")
    public void likes(@PathVariable("postId") String postId) {
        postService.updateLikes(postId);
    }

}
