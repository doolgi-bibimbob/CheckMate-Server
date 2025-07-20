package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.*;

import java.time.LocalDateTime;
import java.util.List;

public record AnswerDetail(
        Long id,
        Integer year,
        double accuracyRate,
        List<String> tagNames,
        String problemTitle,
        String username,
        AnswerStatus status,
        LocalDateTime submittedAt,
        List<String> answerImgSolutions,
        AiReview aiReview,                    // null 가능
        List<UserReviewSummary> userReviewSummaries
) {
    public static AnswerDetail of(Answer a,
                                  AiReview aiReview,
                                  List<UserReviewSummary> userReviews) {

        Problem p = a.getProblem();
        return new AnswerDetail(
                a.getId(),
                p.getExam() != null ? p.getExam().getYear() : null,
                p.getAccuracyRate(),
                p.getTags().stream().map(Tag::getName).toList(),
                p.getTitle(),
                a.getUser().getUsername(),
                a.getStatus(),
                a.getSubmittedAt(),
                List.copyOf(a.getAnswerImgSolutions()),
                aiReview,
                userReviews
        );
    }
    public record AiReview(Long id, String content, int rating, LocalDateTime createdAt) {
        public static AiReview from(Review r) {
            return new AiReview(r.getId(), r.getContent(), r.getRating(), r.getCreatedAt());
        }
    }

    public record UserReviewSummary(Long id, String reviewerName, LocalDateTime createdAt) {
        public static UserReviewSummary from(Review r) {
            return new UserReviewSummary(r.getId(), r.getReviewer().getUsername(), r.getCreatedAt());
        }
    }
}
