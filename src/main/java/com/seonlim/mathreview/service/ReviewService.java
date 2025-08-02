package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.CreateReviewRequest;
import com.seonlim.mathreview.dto.ReviewDetail;
import com.seonlim.mathreview.entity.*;
import com.seonlim.mathreview.repository.AnswerRepository;
import com.seonlim.mathreview.repository.ReviewAnnotationRepository;
import com.seonlim.mathreview.repository.ReviewLayerRepository;
import com.seonlim.mathreview.repository.ReviewRepository;
import com.seonlim.mathreview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewAnnotationRepository annotationRepository;
    private final ReviewLayerRepository reviewLayerRepository;


    @Transactional
    public void createReview(Long answerId, CreateReviewRequest req) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new ResourceNotFoundException("Answer not found: " + answerId));

        CustomUserDetails principal =
                (CustomUserDetails) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();
        User reviewer = principal.getDomain();

        Review review = reviewRepository.save(
                Review.builder()
                        .answer(answer)
                        .reviewer(reviewer)
                        .reviewerType(ReviewerType.STUDENT)
//                        .reviewerType(reviewer.getUserType())
                        .build()
        );
        
        List<ReviewLayer> reviewLayers = req.layers().stream()
                .map(dto -> ReviewLayer.builder()
                        .imgUrl(dto.imgUrl())
                        .pageNumber(dto.pageNumber())
                        .review(review)
                        .build())
                .toList();

        List<ReviewAnnotation> annotations = req.annotations().stream()
                .map(dto -> {
                    ReviewAnnotation ann = ReviewAnnotation.builder()
                            .answer(answer)
                            .imageUrl(dto.imageUrl())
                            .index(dto.index())
                            .content(dto.content())
                            .position(new Position(dto.position().getX(), dto.position().getY()))
                            .width(dto.width())
                            .height(dto.height())
                            .pageNumber(dto.pageNumber())
                            .build();
                    ann.setReview(review);
                    return ann;
                })
                .toList();

        annotationRepository.saveAll(annotations);
        reviewLayerRepository.saveAll(reviewLayers);
    }

    public ReviewDetail getReviewDetail(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰 ID: " + reviewId));
        return ReviewDetail.from(review);
    }

}
