package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.Review;

import java.time.LocalDateTime;

public record MyPageReviewData(
        String targetName,
        String problemTitle,
        LocalDateTime createdAt
) {
    public static MyPageReviewData from(Review review) {
        return new MyPageReviewData(
                review.getAnswer().getUser().getUsername(),
                review.getAnswer().getProblem().getTitle(),
                review.getCreatedAt()
        );
    }
}

