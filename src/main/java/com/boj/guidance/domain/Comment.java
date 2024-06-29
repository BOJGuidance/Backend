package com.boj.guidance.domain;

import com.boj.guidance.util.annotation.LockSerial;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Comment {

    @Id
    @LockSerial
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;
    private String content;
    private String createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment parentComment;
    @OneToMany(mappedBy = "parentComment")
    private List<Comment> commentList = new ArrayList<>();
    @Setter
    private Boolean isDeleted;

    @Builder
    public Comment(
            Post post,
            Member member,
            Comment parentComment,
            String content
    ) {
        this.post = post;
        this.writer = member;
        this.parentComment = parentComment;
        this.content = content;
        this.createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.isDeleted = Boolean.FALSE;
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
    }

    public void deleted() {
        setIsDeleted(Boolean.TRUE);
    }

}
