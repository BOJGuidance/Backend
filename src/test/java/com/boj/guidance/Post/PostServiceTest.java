package com.boj.guidance.Post;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Post;
import com.boj.guidance.domain.enumerate.PostType;
import com.boj.guidance.dto.PostDto.PostCreateRequestDto;
import com.boj.guidance.dto.PostDto.PostResponseDto;
import com.boj.guidance.dto.PostDto.PostUpdateRequestDtp;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.PostRepository;
import com.boj.guidance.service.PostService;
import com.boj.guidance.util.ObjectFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    PostService postService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PostRepository postRepository;

    Member member;

    @BeforeEach
    void setUp() {
        member = ObjectFixtures.getMember();
        given(memberRepository.findById("member")).willReturn(Optional.of(member));
    }

    @Test
    @DisplayName("게시물 생성")
    void createPost() {
        // given
        PostCreateRequestDto createRequestDto = new PostCreateRequestDto("newPostTitle", "newPostContent", PostType.GENERAL);
        Post post = new Post(member, createRequestDto.getTitle(), createRequestDto.getContent(), createRequestDto.getPostType());

        given(postRepository.findById("post")).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        PostResponseDto created = postService.createPost("member", createRequestDto);

        // then
        assertEquals("newPostTitle", created.getTitle());
        assertEquals("newPostContent", created.getContent());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 삭제")
    void deletePost() {
        // given
        Post post = ObjectFixtures.getPost();

        given(postRepository.findById("post")).willReturn(Optional.of(post));
        given(postRepository.save(post)).willReturn(post);

        // when
        PostResponseDto deleted = postService.deletePost("member", "post");

        // then
        assertEquals(true, deleted.getIsDeleted());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("게시물 업데이트")
    void updatePost() {
        // given
        Post post = ObjectFixtures.getPost();
        PostUpdateRequestDtp updateRequestDtp = new PostUpdateRequestDtp("updatedTitle", "updatedContent");

        given(postRepository.findById("post")).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        PostResponseDto updated = postService.updatePost("member", "post", updateRequestDtp);

        // then
        assertEquals("updatedTitle", updated.getTitle());
        assertEquals("updatedContent", updated.getContent());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("좋아요 추가")
    void updateLikes() {
        // given
        Post post = ObjectFixtures.getPost();

        given(postRepository.findById("post")).willReturn(Optional.of(post));
        given(postRepository.save(any(Post.class))).willReturn(post);

        // when
        postService.updateLikes("post");

        // then
        assertEquals(1, post.getLikes());
        verify(postRepository, times(1)).save(any(Post.class));
    }

//    @Test
//    @DisplayName("게시물 조회")
//    void findPostListByPage() {
//        // given
//        // when
//        // then
//    }
}
