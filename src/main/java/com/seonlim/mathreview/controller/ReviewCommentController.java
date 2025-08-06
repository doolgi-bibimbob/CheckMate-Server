package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.ReviewCommentRequest;
import com.seonlim.mathreview.dto.ReviewCommentResponse;
import com.seonlim.mathreview.dto.ReviewCommentUpdateRequest;
import com.seonlim.mathreview.service.ReviewCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review-comments")
@RequiredArgsConstructor
public class ReviewCommentController {
    private final ReviewCommentService reviewCommentService;

    @PostMapping("/{reviewId}")
    public ResponseEntity<Long> createComment(@PathVariable Long reviewId,
                                              @RequestBody ReviewCommentRequest request) {
        Long commentId = reviewCommentService.writeComment(reviewId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        reviewCommentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reviews/comments")
    public ResponseEntity<Void> updateComment(@RequestBody ReviewCommentUpdateRequest request) {
        reviewCommentService.updateComment(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<List<ReviewCommentResponse>> getCommentsByReviewId(@PathVariable Long reviewId) {
        List<ReviewCommentResponse> comments = reviewCommentService.findCommentsByReviewId(reviewId);
        return ResponseEntity.ok(comments);
    }
}
