package com.boj.guidance.util;

import com.boj.guidance.domain.*;
import com.boj.guidance.domain.enumerate.PostType;

public class ObjectFixtures {

    public static Member getMember() {
        return new Member("user1", "user1", "test", "test", 1L, 1L, 1L, 1L, 1L);
    }

    public static Post getPost() {
        return new Post(getMember(), "post1", "test", PostType.GENERAL);
    }

    public static Comment getComment() {
        return new Comment(getPost(), getMember(), null, "comment1");
    }

    public static Comment getChildComment() {
        return new Comment(getPost(), getMember(), getComment(), "childComment1");
    }

    public static Problem getProblem() {
        return new Problem(1000, "problem1", "test", 1, 1L, 1F);
    }

    public static Submission getSubmission() {
        return new Submission("submission1", "test", getMember(), "1", "1000", Boolean.TRUE, "1KB", "1ms", "java11", "1B");
    }

    public static CodeAnalysis getCodeAnalysis() {
        return new CodeAnalysis("1", getMember(), "test", "test", "1000", Boolean.TRUE, "java11");
    }

}
