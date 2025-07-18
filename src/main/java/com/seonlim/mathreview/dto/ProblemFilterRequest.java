package com.seonlim.mathreview.dto;

public record ProblemFilterRequest(
        String title,
        Integer year,
        String tagName,
        Double  minAccuracyRate,
        Double  maxAccuracyRate
) {}
