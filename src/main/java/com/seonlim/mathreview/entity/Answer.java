package com.seonlim.mathreview.entity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    private Problem problem;

    private String imgSolution;

    @ElementCollection
    @CollectionTable(name = "answer_img_solutions", joinColumns = @JoinColumn(name = "answer_id"))
    private List<String> answerImgSolutions = new ArrayList<>();

    private LocalDateTime submittedAt;

    private Long answer;

    private int likeCount;

    @Enumerated(EnumType.STRING)
    private AnswerStatus status;
}

