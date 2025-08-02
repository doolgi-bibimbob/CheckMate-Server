package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.ReviewCommentRequest;
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
                .map(parentId -> {
                    ReviewComment c = commentRepository.findById(parentId)
                            .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다: " + parentId));
                    if (c.getParent() != null) {
                        throw new IllegalArgumentException("대댓글에는 또 다른 대댓글을 달 수 없습니다.");
                    }
                    return c;
                })
                .orElse(null);

        ReviewComment comment = ReviewComment.builder()
                .review(review)
                .author(author)
                .content(request.content())
                .parent(parent)
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

        User currentUser = userRepository.findById(principal.getDomain().getId())
                .orElseThrow(() -> new IllegalArgumentException("유저 정보가 유효하지 않습니다."));

        Optional.of(comment)
                .filter(c -> c.getAuthor().getId().equals(currentUser.getId()))
                .orElseThrow(() -> new SecurityException("본인이 작성한 댓글만 삭제할 수 있습니다."));

        commentRepository.delete(comment);
    }

}
