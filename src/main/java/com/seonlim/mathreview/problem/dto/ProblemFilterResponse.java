package com.seonlim.mathreview.problem.dto;

import java.util.List;

public record ProblemFilterResponse(
        Long problemId,
        String title,
        int year,
        String examName,
        List<String> tagNames,
        double accuracyRate
) {
}
