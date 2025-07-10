package com.seonlim.mathreview.user.dto;

public record RegisterRequest(
        String email,
        String password,
        String username) {
}
