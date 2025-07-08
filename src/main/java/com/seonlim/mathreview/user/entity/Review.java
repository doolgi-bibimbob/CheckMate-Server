package com.seonlim.mathreview.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "solution_id")
    private Answer answer;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @Enumerated(EnumType.STRING)
    private ReviewerType reviewerType;

    @Lob
    private String content;

    private int rating;

    private LocalDateTime createdAt;
}
