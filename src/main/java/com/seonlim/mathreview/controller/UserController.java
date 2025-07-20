package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.MyPageAnswerData;
import com.seonlim.mathreview.dto.MyPageProfileUpdateRequest;
import com.seonlim.mathreview.dto.MyPageReviewData;
import com.seonlim.mathreview.dto.MyPageUserData;
import com.seonlim.mathreview.security.CustomUserDetails;
import com.seonlim.mathreview.service.UserService;
import jakarta.validation.constraints.Pattern;
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

    @PostMapping("/my-page/password-update")
    public ResponseEntity<Void> updateUserPassword(

            @Pattern(
                    regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\\W_]).{8,20}$",
                    message = "비밀번호는 영문자·숫자·특수문자를 모두 포함해 8~20자여야 합니다."
            )
            @RequestBody String newPassword,

            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.updatePassword(newPassword, userDetails.getDomain().getId());
        return ResponseEntity.ok().build();
    }
}
