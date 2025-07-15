package com.seonlim.mathreview.user.dto;

public record VerifyCodeRequest(
        String email,
        String code
) {
}
