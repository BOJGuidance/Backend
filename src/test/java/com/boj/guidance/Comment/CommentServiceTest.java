package com.boj.guidance.Comment;

import com.boj.guidance.domain.Comment;
import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Post;
import com.boj.guidance.dto.CommentDto.CommentCreateRequestDto;
import com.boj.guidance.dto.CommentDto.CommentResponseDto;
import com.boj.guidance.dto.CommentDto.CommentsResponseDto;
import com.boj.guidance.repository.CommentRepository;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.PostRepository;
import com.boj.guidance.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.boj.guidance.util.ObjectFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class CommentServiceTest {

    @Autowired
    CommentService commentService;

    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PostRepository postRepository;

    @MockBean
    CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        Member member = getMember();
        Post post = getPost();
        given(memberRepository.findById("member")).willReturn(Optional.of(member));
        given(postRepository.findById("post")).willReturn(Optional.of(post));
    }

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        // given
        CommentCreateRequestDto createRequestDto = new CommentCreateRequestDto("member", null, "childComment1");
        Comment comment = getComment();

        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        CommentResponseDto created = commentService.createComment("post", createRequestDto);

        // then
        assertEquals("comment1", created.getContent());
        assertEquals(getMember().getHandle(), created.getWriter());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("대댓글 생성")
    void createChildComment() {
        // given
        Comment comment = getComment();
        Comment childComment = getChildComment();
        CommentCreateRequestDto createRequestDto = new CommentCreateRequestDto("member", "comment", "childComment1");

        given(commentRepository.save(any(Comment.class))).willReturn(childComment);
        given(commentRepository.findById("comment")).willReturn(Optional.of(comment));

        // when
        CommentResponseDto created = commentService.createChildComment("post", createRequestDto);

        // then
        assertEquals(1, comment.getCommentList().size());
        assertEquals("childComment1", comment.getCommentList().get(0).getContent());
        assertEquals("comment1", childComment.getParentComment().getContent());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
        // given
        Comment comment = getComment();
        comment.getWriter().setId("member");
        comment.getPost().setId("post");

        given(commentRepository.findById("comment")).willReturn(Optional.of(comment));

        // when
        CommentResponseDto deleted = commentService.deleteComment("member", "comment");

        // then
        assertTrue(deleted.getIsDeleted());
    }

    @Test
    @DisplayName("대댓글 삭제")
    void deleteChildComment() {
        // given
        Comment comment = getComment();
        Comment childComment = getChildComment();
        childComment.getWriter().setId("member");
        childComment.getPost().setId("post");

        given(commentRepository.findById("comment")).willReturn(Optional.of(comment));
        given(commentRepository.findById("childComment")).willReturn(Optional.of(childComment));

        // when
        CommentResponseDto deleted = commentService.deleteComment("member", "childComment");

        // then
        assertTrue(deleted.getIsDeleted());
        assertEquals(0, comment.getCommentList().size());
    }

    @Test
    @DisplayName("게시물에 대한 댓글 조회")
    void commentsOfPost() {
        // given
        List<Comment> list = new ArrayList<>();
        Comment comment1 = getComment(); list.add(comment1);
        Comment comment2 = getComment(); list.add(comment2);
        Comment childComment = getChildComment(); list.add(childComment);

        given(commentRepository.findAllByPostId("post")).willReturn(list);

        // when
        CommentsResponseDto result = commentService.commentsOfPost("post");

        // then
        assertEquals(3, result.getCount());
        assertEquals(3, result.getCommentList().size());
    }

}
