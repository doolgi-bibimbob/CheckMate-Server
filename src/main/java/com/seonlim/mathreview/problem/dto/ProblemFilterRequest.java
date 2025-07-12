package com.seonlim.mathreview.problem.dto;

public record ProblemFilterRequest(
        String title,
        Integer year,
        String tagName,
        Double minAccuracyRate
) {}
