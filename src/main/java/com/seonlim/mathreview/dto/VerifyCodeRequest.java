package com.seonlim.mathreview.dto;

public record VerifyCodeRequest(
        String email,
        String code
) {
}
