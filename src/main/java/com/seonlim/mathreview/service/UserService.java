package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.MyPageAnswerData;
import com.seonlim.mathreview.dto.MyPageProfileUpdateRequest;
import com.seonlim.mathreview.dto.MyPageReviewData;
import com.seonlim.mathreview.dto.MyPageUserData;
import com.seonlim.mathreview.entity.User;
import com.seonlim.mathreview.repository.AnswerRepository;
import com.seonlim.mathreview.repository.ReviewRepository;
import com.seonlim.mathreview.repository.UserRepository;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;


    public MyPageUserData getMyPageUserData(Long userId) {
        return userRepository.findById(userId)
                .map(MyPageUserData::from)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    public List<MyPageAnswerData> getMyPageAnswerData(Long userId) {
        return Optional.of(answerRepository.findAllByUserId(userId))
                .filter(list -> !list.isEmpty())
                .map(list -> list.stream()
                        .map(MyPageAnswerData::from)
                        .toList())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No answers found for user with id: " + userId));
    }

    public List<MyPageReviewData> getMyPageReviewData(Long userId) {
        return reviewRepository.findAllByReviewerId(userId)
                .stream()
                .map(MyPageReviewData::from)
                .toList();
    }

    public void updateUserProfile(MyPageProfileUpdateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Optional.ofNullable(request.userName())
                .ifPresent(user::setUsername);

        Optional.ofNullable(request.profileImgUrl())
                .ifPresent(user::setProfileImageUrl);

        userRepository.save(user);
    }

    public void updatePassword(String newPassword, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }
}
