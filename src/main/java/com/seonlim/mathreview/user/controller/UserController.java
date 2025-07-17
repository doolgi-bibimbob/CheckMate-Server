package com.seonlim.mathreview.user.controller;

import com.seonlim.mathreview.user.dto.MyPageAnswerData;
import com.seonlim.mathreview.user.dto.MyPageReviewData;
import com.seonlim.mathreview.user.dto.MyPageUserData;
import com.seonlim.mathreview.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("my-page/user-data")
    public ResponseEntity<MyPageUserData> getMyPageUserData(@RequestParam Long userId) {
        MyPageUserData response = userService.getMyPageUserData(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("my-page/answer-data")
    public List<MyPageAnswerData> getUserAnswers(@RequestParam Long userId) {
        return userService.getMyPageAnswerData(userId);
    }

    @GetMapping("my-page/review-data")
    public ResponseEntity<List<MyPageReviewData>> getUserReviewData(@RequestParam Long userId) {
        List<MyPageReviewData> response = userService.getMyPageReviewData(userId);
        return ResponseEntity.ok(response);
    }
}
