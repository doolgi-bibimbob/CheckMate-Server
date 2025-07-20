package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.CreateReviewRequest;
import com.seonlim.mathreview.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/answers/{answerId}/reviews")
    public ResponseEntity<Void> createReview(@PathVariable Long answerId,
                                             @RequestBody CreateReviewRequest request) {
        reviewService.createReview(answerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
