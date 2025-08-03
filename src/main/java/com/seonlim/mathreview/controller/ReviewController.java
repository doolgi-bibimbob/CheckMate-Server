package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.CreateReviewRequest;
import com.seonlim.mathreview.dto.ReviewDetail;
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

    @GetMapping("/review-detail/{reviewId}")
    public ResponseEntity<ReviewDetail> getReviewDetail(@PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewService.getReviewDetail(reviewId));
    }

    @PutMapping("/answers/{answerId}/reviews/{reviewId}")
    public ResponseEntity<Void> updateReview(@PathVariable Long answerId,
                                             @PathVariable Long reviewId,
                                             @RequestBody CreateReviewRequest request) {
        reviewService.updateReview(answerId, reviewId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

}
