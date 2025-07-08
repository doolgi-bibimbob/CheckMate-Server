package com.seonlim.mathreview.user.repository;

import com.seonlim.mathreview.user.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
