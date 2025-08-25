package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.*;
import com.seonlim.mathreview.security.CustomUserDetails;
import com.seonlim.mathreview.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/my-page/user-data")
    public ResponseEntity<MyPageUserData> getMyPageUserData(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MyPageUserData response = userService.getMyPageUserData(userDetails.getDomain().getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-page/answer-data")
    public List<MyPageAnswerData> getUserAnswers(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return userService.getMyPageAnswerData(userDetails.getDomain().getId());
    }

    @GetMapping("/my-page/review-data")
    public ResponseEntity<List<MyPageReviewData>> getUserReviewData(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<MyPageReviewData> response = userService.getMyPageReviewData(userDetails.getDomain().getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/my-page/profile-update")
    public ResponseEntity<Void> updateUserProfile(
            @RequestBody MyPageProfileUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.updateUserProfile(request, userDetails.getDomain().getId());
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/my-page/password-update")
//    public ResponseEntity<Void> updateUserPassword(
//            @RequestBody @Valid PasswordUpdateRequest req,
//            @AuthenticationPrincipal CustomUserDetails userDetails
//    ) {
//        userService.updatePassword(req.newPassword(), userDetails.getDomain().getId());
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/password/code")
    public void sendPwCode(@RequestParam String email) {
        userService.sendPasswordUpdateCode(email);
    }

    @PostMapping("/password/verify")
    public void verifyPwCode(@RequestParam String email, @RequestParam String code) {
        userService.verifyPasswordUpdateCode(email, code);
    }

    @PostMapping("/password/update")
    public void updatePw(@RequestParam String email, @RequestParam String newPassword) {
        userService.updatePassword(email, newPassword);
    }

}
