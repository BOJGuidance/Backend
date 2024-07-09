package com.boj.guidance.dto.PostDto;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Post;
import com.boj.guidance.domain.enumerate.PostType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequestDto {
    private String title;
    private String content;
    private PostType postType;

    public Post toEntity(Member writer) {
        return Post.builder()
                .writer(writer)
                .title(this.title)
                .content(this.content)
                .postType(this.postType)
                .build();
    }
}
