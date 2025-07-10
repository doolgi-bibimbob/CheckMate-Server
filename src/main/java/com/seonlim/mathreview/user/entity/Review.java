package com.seonlim.mathreview.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "amswer_id")
    private Answer answer;

    @ManyToOne(optional = true)
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @Enumerated(EnumType.STRING)
    private ReviewerType reviewerType;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int rating;

    private LocalDateTime createdAt;
}
