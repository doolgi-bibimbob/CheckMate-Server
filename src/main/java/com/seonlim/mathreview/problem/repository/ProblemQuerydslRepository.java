package com.seonlim.mathreview.problem.repository;

import com.seonlim.mathreview.problem.dto.ProblemFilterResponse;

import java.util.List;

public interface ProblemQuerydslRepository {
    List<ProblemFilterResponse> filterProblems(String title, Integer year, String tagName, Double minAccuracyRate);
}
