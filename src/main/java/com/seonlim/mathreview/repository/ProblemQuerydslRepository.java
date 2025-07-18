package com.seonlim.mathreview.repository;

import com.seonlim.mathreview.dto.ProblemFilterResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemQuerydslRepository {
    List<ProblemFilterResponse> filterProblems(String title, Integer year, String tagName, Double minAccuracyRate);
}
