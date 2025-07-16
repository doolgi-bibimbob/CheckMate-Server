package com.seonlim.mathreview.user.controller;

import com.seonlim.mathreview.user.dto.MyPageUserResponse;
import com.seonlim.mathreview.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("my-page")
    public ResponseEntity<MyPageUserResponse> getMyPageUserData(@RequestParam Long userId) {
        MyPageUserResponse response = userService.getMyPageUserData(userId);
        return ResponseEntity.ok(response);
    }
}
