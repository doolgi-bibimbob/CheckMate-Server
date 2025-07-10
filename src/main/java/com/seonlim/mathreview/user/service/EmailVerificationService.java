package com.seonlim.mathreview.user.service;

import com.seonlim.mathreview.user.cache.EmailVerificationCache;
import com.seonlim.mathreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private final EmailVerificationCache cache;
    private final UserRepository userRepository;

    public void sendCode(String email) {
        String code = String.format("%06d", new Random().nextInt(999999));
        cache.saveCode(email, code);
        System.out.println("📤 인증 코드 전송: " + code + " → " + email);
    }

    public boolean verifyCode(String email, String code) {
        return Optional.of(email)
                .filter(e -> !userRepository.existsByEmail(e))
                .map(e -> cache.verify(e, code))
                .orElseThrow(() -> new IllegalArgumentException("이미 사용 중인 이메일이거나 인증 코드가 유효하지 않습니다."));
    }

    public boolean isVerified(String email) {
        return cache.isVerified(email);
    }

    public void invalidate(String email) {
        cache.invalidate(email);
    }
}
