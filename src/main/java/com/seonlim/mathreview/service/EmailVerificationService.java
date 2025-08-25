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

    private String signupKey(String email) { return "SIGNUP:" + email; }
    private String pwKey(String email)     { return "PW:" + email; }

    public void sendCode(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        String code = randomCode();
        cache.saveCode(signupKey(email), code);
        sendSignupEmail(email, code);
        System.out.println("📤 (SIGNUP) 인증 코드 전송: " + code + " → " + email);
    }

    public boolean verifyCode(String email, String code) {
        return Optional.of(email)
                .filter(e -> !userRepository.existsByEmail(e))
                .map(e -> cache.verify(signupKey(e), code))
                .orElseThrow(() -> new IllegalArgumentException("이미 사용 중인 이메일이거나 인증 코드가 유효하지 않습니다."));
    }

    public boolean isVerified(String email) {
        return cache.isVerified(signupKey(email));
    }

    public void invalidate(String email) {
        cache.invalidate(signupKey(email));
    }

    public void sendPasswordUpdateCode(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("해당 이메일의 사용자가 없습니다.");
        }
        String code = randomCode();
        cache.saveCode(pwKey(email), code);
        sendPwUpdateEmail(email, code);
        System.out.println("📤 (PW_RESET) 인증 코드 전송: " + code + " → " + email);
    }

    public boolean verifyPasswordUpdateCode(String email, String code) {
        return Optional.of(email)
                .filter(userRepository::existsByEmail)
                .map(e -> cache.verify(pwKey(e), code))
                .orElseThrow(() -> new IllegalArgumentException("사용자가 없거나 인증 코드가 유효하지 않습니다."));
    }

    public boolean isPasswordUpdateVerified(String email) {
        return cache.isVerified(pwKey(email));
    }

    public void invalidatePasswordUpdate(String email) {
        cache.invalidate(pwKey(email));
    }

    private String randomCode() {
        return String.format("%06d", new Random().nextInt(1_000_000));
    }

    private void sendSignupEmail(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("[CheckMate] 이메일 인증 코드");
        msg.setText("인증 코드: " + code + "\n\n본 코드는 5분간 유효합니다.");
        mailSender.send(msg);
    }

    private void sendPwUpdateEmail(String to, String code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("[CheckMate] 비밀번호 변경 인증 코드");
        msg.setText("비밀번호 변경 인증 코드: " + code + "\n\n본 코드는 5분간 유효합니다.");
        mailSender.send(msg);
    }
}
