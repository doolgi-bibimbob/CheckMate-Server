package com.seonlim.mathreview.dto;

public record ReviewCommentUpdateRequest(
        Long commentId,
        String content
) {
}
