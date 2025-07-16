package com.seonlim.mathreview.user.controller;

import com.seonlim.mathreview.security.JwtCookieAuthenticationFilter;
import com.seonlim.mathreview.security.JwtTokenProvider;
import com.seonlim.mathreview.user.dto.Login;
import com.seonlim.mathreview.user.dto.RegisterRequest;
import com.seonlim.mathreview.user.dto.SendCodeRequest;
import com.seonlim.mathreview.user.dto.VerifyCodeRequest;
import com.seonlim.mathreview.user.entity.User;
import com.seonlim.mathreview.user.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private static final String COOKIE = JwtCookieAuthenticationFilter.COOKIE_NAME;

    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestBody SendCodeRequest dto) {
        authService.sendVerificationCode(dto.email());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyCodeRequest dto) {
        authService.verifyEmail(dto.email(), dto.code());
        return ResponseEntity.ok("✅ 인증 성공");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegisterRequest request) {
        User user = authService.signup(request);
        return ResponseEntity.ok("✅ 회원가입 성공! ID=" + user.getId());
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Login dto, HttpServletResponse res) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        String jwt = tokenProvider.generateToken(auth);

        ResponseCookie cookie = ResponseCookie.from(COOKIE, jwt)
                .httpOnly(true).secure(true).sameSite("Lax")
                .path("/").maxAge(Duration.ofHours(1)).build();

        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse res) {
        ResponseCookie expire = ResponseCookie.from(COOKIE, "")
                .httpOnly(true).secure(true).path("/").maxAge(0).build();
        res.addHeader(HttpHeaders.SET_COOKIE, expire.toString());
        return ResponseEntity.ok().build();
    }
}
