package com.seonlim.mathreview.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long problemId;

    private String imgSolution;

    private LocalDateTime submittedAt;

    private Long answer;

    private int likeCount;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status;
}

