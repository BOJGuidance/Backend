package com.boj.guidance.service.implement;

import com.boj.guidance.domain.Member;
import com.boj.guidance.domain.Post;
import com.boj.guidance.dto.PostDto.*;
import com.boj.guidance.repository.MemberRepository;
import com.boj.guidance.repository.PostRepository;
import com.boj.guidance.service.PostService;
import com.boj.guidance.util.api.ResponseCode;
import com.boj.guidance.util.exception.MemberException;
import com.boj.guidance.util.exception.PostException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final RedissonClient redissonClient;

    @Override
    public PostResponseDto createPost(String creator, PostCreateRequestDto dto) {
        Member member = getMember(creator);
        return new PostResponseDto().toDto(postRepository.save(dto.toEntity(member.getHandle())));
    }

    @Override
    public PostResponseDto deletePost(String deleter, String postId) {
        Member member = getMember(deleter); // validate logic 추가 필요
        Post post = getPost(postId);
        post.deleted();
        return new PostResponseDto().toDto(post);
    }

    @Override
    public PostResponseDto updatePost(String updater, String postId, PostUpdateRequestDtp dto) {
        Member member = getMember(updater);
        Post post = getPost(postId);
        post.update(dto.getTitle(), dto.getContent());
        return new PostResponseDto().toDto(post);
    }

    @Override
    public void updateLikes(String postId) {
        Post post = getPost(postId);
        String lockKey = "updateLikes" + postId;
        final RLock rLock = redissonClient.getLock(lockKey);

        try {
            if (!rLock.tryLock(1, 3, TimeUnit.SECONDS)) return;
            post.addLikes();
            log.info("Likes Updated: {}", post.getLikes().toString());
        } catch (InterruptedException e) {
            throw new PostException(ResponseCode.POST_LIKES_UPDATE_FAIL);
        } finally {
            if (rLock.isLocked()) rLock.unlock();
        }
    }

    @Override
    public PostsResponseDto findPostListByPage(Integer currentPage, Integer page) {
        return null;
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
