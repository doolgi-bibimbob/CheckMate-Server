package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.entity.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @EntityGraph(attributePaths = { "answer.user", "answer.problem" })
    List<Review> findAllByReviewerId(Long reviewerId);
}
