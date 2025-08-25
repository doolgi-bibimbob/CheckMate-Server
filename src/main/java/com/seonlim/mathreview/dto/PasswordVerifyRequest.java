package com.seonlim.mathreview.dto;

public record PasswordVerifyRequest(
        String email,
        String code
) {
}
