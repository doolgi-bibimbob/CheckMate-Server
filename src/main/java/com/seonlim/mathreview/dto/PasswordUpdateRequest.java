package com.seonlim.mathreview.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateRequest(
//        @NotBlank
//        @Pattern(
//                regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,20}$",
//                message = "비밀번호는 영문자와 숫자를 모두 포함해 8~20자여야 합니다."
//        )
        String email,
        String newPassword
) {}
