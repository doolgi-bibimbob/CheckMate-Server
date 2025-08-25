package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.*;
import com.seonlim.mathreview.entity.User;
import com.seonlim.mathreview.exception.SamePasswordException;
import com.seonlim.mathreview.exception.VerificationException;
import com.seonlim.mathreview.repository.AnswerRepository;
import com.seonlim.mathreview.repository.ReviewRepository;
import com.seonlim.mathreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationService emailVerificationService;


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
                .orElse(Collections.emptyList());
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

    @Transactional
    public void updatePassword(String email, String newPassword) {
        User user = Optional.ofNullable(email)
                .filter(emailVerificationService::isPasswordUpdateVerified)
                .flatMap(userRepository::findByEmail)
                .orElseThrow(() ->
                        new VerificationException("비밀번호 변경 인증 미완료 또는 사용자가 존재하지 않습니다."));

        Optional.of(user)
                .filter(u -> !passwordEncoder.matches(newPassword, u.getPassword()))
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(newPassword));
                    return userRepository.save(u);
                })
                .orElseThrow(() -> new SamePasswordException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다."));

        emailVerificationService.invalidatePasswordUpdate(user.getEmail());

        Authentication testAuth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), newPassword));
        log.info("⏩ 내부 인증 성공 여부 = {}", testAuth.isAuthenticated());
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        userRepository.delete(user);
    }

    public void sendPasswordUpdateCode(PasswordCodeRequest request) {
        if (!userRepository.existsByEmail(request.email())) {
            throw new ResourceNotFoundException("해당 이메일의 사용자가 없습니다.");
        }
        emailVerificationService.sendPasswordUpdateCode(request.email());
    }

    public void verifyPasswordUpdateCode(String email, String code) {
        Optional.of(emailVerificationService.verifyPasswordUpdateCode(email, code))
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> new VerificationException("비밀번호 변경용 인증 코드가 유효하지 않습니다."));
    }
}
