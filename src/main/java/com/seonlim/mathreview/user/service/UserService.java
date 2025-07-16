package com.seonlim.mathreview.user.service;

import com.seonlim.mathreview.user.dto.MyPageUserResponse;
import com.seonlim.mathreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public MyPageUserResponse getMyPageUserData(Long userId) {
        return userRepository.findById(userId)
                .map(MyPageUserResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }
}
