package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.dto.ProblemFilterResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemQuerydslRepository {
    Page<ProblemFilterResponse> filterProblems(
            String title,
            Integer year,
            String tagName,
            Double  minAccuracyRate,
            Double  maxAccuracyRate,
            Pageable pageable
    );
}
