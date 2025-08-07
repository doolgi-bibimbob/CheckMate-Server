package com.seonlim.mathreview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seonlim.mathreview.entity.ReviewComment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record ReviewCommentResponse(
        Long id,
        Long authorId,
        String authorName,
        String content,

        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                timezone = "UTC"
        )
        LocalDateTime createdAt,

        Long parentId,
        String profileImgUrl,
        String parentAuthorName,
        Boolean isDeleted,
        List<ReviewCommentResponse> children
) {
    public static ReviewCommentResponse from(ReviewComment comment) {
        List<ReviewCommentResponse> children = comment.getChildren().stream()
                .map(ReviewCommentResponse::from)
                .toList();

        Long parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        String parentAuthorName = comment.getParent() != null ? comment.getParent().getAuthor().getUsername() : null;
        String profileImgUrl = comment.getAuthor() != null ? comment.getAuthor().getProfileImageUrl() : null;

        return new ReviewCommentResponse(
                comment.getId(),
                Objects.requireNonNull(comment.getAuthor()).getId(),
                comment.getAuthor().getUsername(),
                comment.getContent(),
                comment.getCreatedAt(),
                parentId,
                comment.getAuthor().getProfileImageUrl(),
                parentAuthorName,
                comment.isDeleted(),
                children
        );
    }
}
