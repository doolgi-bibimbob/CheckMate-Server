package com.seonlim.mathreview.user.controller;

import com.seonlim.mathreview.user.dto.RegisterRequest;
import com.seonlim.mathreview.user.entity.User;
import com.seonlim.mathreview.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestParam String email) {
        authService.sendVerificationCode(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        authService.verifyEmail(email, code);
        return ResponseEntity.ok("✅ 인증 성공");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest request) {
        User user = authService.signup(request);
        return ResponseEntity.ok("✅ 회원가입 성공! ID=" + user.getId());
    }
}
