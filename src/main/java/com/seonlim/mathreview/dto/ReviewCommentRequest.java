package com.seonlim.mathreview.dto;

public record ReviewCommentRequest(
        Long parentId,
        String content
) {
}
