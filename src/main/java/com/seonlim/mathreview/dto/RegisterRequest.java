package com.seonlim.mathreview.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @Email
        String email,

        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}$",
                message = "비밀번호는 영문자와 숫자를 모두 포함해 8~20자여야 합니다."
        )
        String password,

        String username) {
}
