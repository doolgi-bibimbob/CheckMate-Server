package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.ReviewCommentRequest;
import com.seonlim.mathreview.dto.ReviewCommentResponse;
import com.seonlim.mathreview.dto.ReviewCommentUpdateRequest;
import com.seonlim.mathreview.entity.Review;
import com.seonlim.mathreview.entity.ReviewComment;
import com.seonlim.mathreview.entity.User;
import com.seonlim.mathreview.repository.ReviewCommentRepository;
import com.seonlim.mathreview.repository.ReviewRepository;
import com.seonlim.mathreview.repository.UserRepository;
import com.seonlim.mathreview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long writeComment(Long reviewId, ReviewCommentRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + reviewId));

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        User author = userRepository.findById(principal.getDomain().getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 유효하지 않습니다."));

        ReviewComment parent = Optional.ofNullable(request.parentId())
                .map(parentId -> commentRepository.findById(parentId)
                        .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다: " + parentId)))
                .orElse(null);

        ReviewComment comment = ReviewComment.builder()
                .review(review)
                .author(author)
                .content(request.content())
                .parent(parent)
                .deleted(false)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }

    @Transactional
    public void deleteComment(Long commentId) {
        ReviewComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다: " + commentId));

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        Long currentUserId = principal.getDomain().getId();

        if (!comment.getAuthor().getId().equals(currentUserId)) {
            throw new SecurityException("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        comment.setDeleted(true);
//        comment.setContent("삭제된 댓글입니다.");
    }

    @Transactional(readOnly = true)
    public List<ReviewCommentResponse> findCommentsByReviewId(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다: " + reviewId));

        return commentRepository.findByReview(review).stream()
                .filter(c -> c.getParent() == null)
                .map(ReviewCommentResponse::from)
                .toList();
    }

    @Transactional
    public void updateComment(ReviewCommentUpdateRequest request) {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        Long currentUserId = principal.getDomain().getId();

        ReviewComment comment = commentRepository.findById(request.commentId())
                .filter(c -> !c.isDeleted())
                .filter(c -> c.getAuthor().getId().equals(currentUserId))
                .orElseThrow(() -> new SecurityException("삭제되었거나 권한이 없는 댓글입니다."));

        comment.setContent(request.content());
    }


}
