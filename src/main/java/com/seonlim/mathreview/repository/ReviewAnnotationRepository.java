package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.entity.ReviewAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewAnnotationRepository extends JpaRepository<ReviewAnnotation, Long> {
}
