package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.entity.Review;
import com.seonlim.mathreview.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {
    List<ReviewComment> findByReview(Review review);
}
