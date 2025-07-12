package com.seonlim.mathreview.problem.repository;

import com.seonlim.mathreview.problem.dto.ProblemFilterResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProblemQuerydslRepository {
    List<ProblemFilterResponse> filterProblems(String title, Integer year, String tagName, Double minAccuracyRate);
}
