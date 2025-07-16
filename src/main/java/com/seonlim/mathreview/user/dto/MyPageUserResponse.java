package com.seonlim.mathreview.user.dto;

import com.seonlim.mathreview.user.entity.User;
import com.seonlim.mathreview.user.entity.UserType;

public record MyPageUserResponse(
        Long id,
        String username,
        String email,
        UserType userType,
        String profileImageUrl
) {
    public static MyPageUserResponse from(User user) {
        return new MyPageUserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserType(),
                user.getProfileImageUrl()
        );
    }
}
