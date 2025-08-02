package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.Review;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetail(
        Long id,
        Long answerId,
        Long reviewerId,
        String reviewerName,
        String reviewerType,
        String aiReviewContent,
        LocalDateTime createdAt,
        List<ReviewAnnotationDto> annotations

) {
    public static ReviewDetail from(Review review) {
        return new ReviewDetail(
                review.getId(),
                review.getAnswer().getId(),
                review.getReviewer() != null ? review.getReviewer().getId() : null,
                review.getReviewer() != null ? review.getReviewer().getUsername() : null,
                review.getReviewerType() != null ? review.getReviewerType().name() : null,
                review.getAiReviewContent(),
                review.getCreatedAt(),
                review.getAnnotations().stream()
                        .map(ReviewAnnotationDto::from)
                        .toList()
        );
    }
}
