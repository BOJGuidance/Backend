package com.boj.guidance.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Problem {
    @Id
    private Integer problemId;
    private String title;
    private String link;
    private int level;
    private Long numberOfSolved;
    private float avgTry;
}
