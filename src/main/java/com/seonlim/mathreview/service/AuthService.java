package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.RegisterRequest;
import com.seonlim.mathreview.entity.User;
import com.seonlim.mathreview.entity.UserType;
import com.seonlim.mathreview.exception.VerificationException;
import com.seonlim.mathreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailVerificationService emailVerificationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendVerificationCode(String email) {
        emailVerificationService.sendCode(email);
    }

    public void verifyEmail(String email, String code) {
        Optional.of(emailVerificationService.verifyCode(email, code))
                .filter(Boolean::booleanValue)
                .orElseThrow(() -> new VerificationException("인증 코드가 유효하지 않습니다."));
    }

    public User signup(RegisterRequest request) {
        return Optional.ofNullable(request.email())
                .filter(emailVerificationService::isVerified)
                .flatMap(email -> {
                    if (userRepository.existsByEmail(email)) return Optional.empty();
                    User user = User.builder()
                            .email(email)
                            .username(request.username())
                            .password(passwordEncoder.encode(request.password()))
                            .userType(UserType.STUDENT)
                            .build();
                    return Optional.of(user);
                })
                .map(userRepository::save)
                .map(saved -> {
                    emailVerificationService.invalidate(request.email());
                    return saved;
                })
                .orElseThrow(() -> new VerificationException("이메일 인증 실패 또는 중복된 이메일입니다."));
    }
}