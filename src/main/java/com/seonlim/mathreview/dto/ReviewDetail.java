package com.seonlim.mathreview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seonlim.mathreview.entity.Review;
import com.seonlim.mathreview.entity.ReviewLayer;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewDetail(
        Long id,
        Long answerId,
        Long reviewerId,
        String reviewerName,
        String reviewerType,
        String aiReviewContent,
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                timezone = "UTC"
        )
        LocalDateTime createdAt,
        List<ReviewAnnotationDto> annotations,
        List<ReviewLayerDto> layers

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
                        .toList(),
                review.getLayers().stream()
                        .map(ReviewLayerDto::from)
                        .toList()
        );
    }
}
