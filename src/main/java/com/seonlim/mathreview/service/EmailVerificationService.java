package com.seonlim.mathreview.service;

import com.seonlim.mathreview.cache.EmailVerificationCache;
import com.seonlim.mathreview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationService {
    private final EmailVerificationCache cache;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    public void sendCode(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        cache.saveCode(email, code);
        sendEmail(email, code);
        System.out.println("📤 인증 코드 전송: " + code + " → " + email);
    }


    private void sendEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[MathReview] 이메일 인증 코드");
        message.setText("인증 코드: " + code + "\n\n본 코드는 5분간 유효합니다.");
        mailSender.send(message);
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
