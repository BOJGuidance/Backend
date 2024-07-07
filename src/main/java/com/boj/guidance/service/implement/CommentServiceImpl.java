package com.boj.guidance.service.implement;

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
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.CommentException;
import com.boj.guidance.util.exception.MemberException;
import com.boj.guidance.util.exception.PostException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto createComment(String memberId, String postId, CommentCreateRequestDto dto) {
        Member creator = getMember(memberId);
        Post post = getPost(postId);
        Comment comment = dto.toEntity(creator, post, null);
        creator.addComment(comment);
        post.addComment(comment);
        return new CommentResponseDto().toDto(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto createChildComment(String memberId, String postId, String commentId, CommentCreateRequestDto dto) {
        Member creator = getMember(memberId);
        Post post = getPost(postId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new CommentException(ResponseCode.POST_NOT_FOUND)
                );
        Comment childComment = dto.toEntity(creator, post, comment);

        creator.addComment(comment);
        post.addComment(comment);
        comment.addComment(childComment);
        return new CommentResponseDto().toDto(commentRepository.save(childComment));
    }

    @Override
    public CommentResponseDto deleteComment(String memberId, String commentId) {
        Member deleter = getMember(memberId);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(
                        () -> new CommentException(ResponseCode.COMMENT_NOT_FOUND)
                );
        Member writer = getMember(comment.getWriter().getId()); writer.delComment(comment);
        Post post = getPost(comment.getPost().getId()); post.delComment(comment);
        Comment parentComment = comment.getParentComment();
        if (parentComment != null) parentComment.deleteComment(comment);
        comment.deleted();
        return new CommentResponseDto().toDto(comment);
    }

    @Override
    public CommentsResponseDto commentsOfPost(String postId) {
        List<CommentResponseDto> comments = commentRepository.findAllByPostId(postId).stream()
                .map(comment -> new CommentResponseDto().toDto(comment))
                .toList();
        Integer count = comments.size();
        return new CommentsResponseDto().toDtos(comments, count);
    }

    public Member getMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(
                        () -> new MemberException(ResponseCode.MEMBER_NOT_EXIST)
                );
    }

    public Post getPost(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(
                        () -> new PostException(ResponseCode.POST_NOT_FOUND)
                );
    }
}
