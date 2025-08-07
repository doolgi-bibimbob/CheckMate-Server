package com.seonlim.mathreview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seonlim.mathreview.entity.Review;

import java.time.LocalDateTime;

public record MyPageReviewData(
        String targetName,
        String problemTitle,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                timezone = "UTC"
        )
        LocalDateTime createdAt,
        Long problemId,
        Long reviewId,
        Long answerId
) {
    public static MyPageReviewData from(Review review) {
        return new MyPageReviewData(
                review.getAnswer().getUser().getUsername(),
                review.getAnswer().getProblem().getTitle(),
                review.getCreatedAt(),
                review.getAnswer().getProblem().getId(),
                review.getId(),
                review.getAnswer().getId()
        );
    }
}

