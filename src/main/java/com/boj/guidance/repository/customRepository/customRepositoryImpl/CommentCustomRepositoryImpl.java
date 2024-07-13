package com.boj.guidance.repository.customRepository.customRepositoryImpl;

import com.boj.guidance.domain.Comment;
import com.boj.guidance.repository.customRepository.CommentCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.boj.guidance.domain.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> findAllByPostId(String postId) {
        return jpaQueryFactory
                .selectFrom(comment)
                .where(comment.post.id.eq(postId))
                .fetch();
    }
}
