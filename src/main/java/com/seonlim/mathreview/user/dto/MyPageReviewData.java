package com.seonlim.mathreview.user.dto;

import com.seonlim.mathreview.user.entity.Review;

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

