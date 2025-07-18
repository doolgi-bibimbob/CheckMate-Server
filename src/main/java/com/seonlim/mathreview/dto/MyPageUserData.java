package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.User;
import com.seonlim.mathreview.entity.UserType;

public record MyPageUserData(
        Long id,
        String username,
        String email,
        UserType userType,
        String profileImageUrl
) {
    public static MyPageUserData from(User user) {
        return new MyPageUserData(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getUserType(),
                user.getProfileImageUrl()
        );
    }
}
