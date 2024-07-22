package com.boj.guidance.dto.CommentDto;

import com.boj.guidance.domain.Comment;
import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreateRequestDto {
    private String memberId;
    private String parentCommentId;
    private String content;

    public Comment toEntity(Member member, Post post, Comment parentComment) {
        return Comment.builder()
                .member(member)
                .post(post)
                .parentComment(parentComment)
                .content(this.content)
                .build();
    }
}
