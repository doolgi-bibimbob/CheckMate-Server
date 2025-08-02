package com.seonlim.mathreview.dto;

import java.util.List;

public record ProblemFilterResponse(
        Long problemId,
        String title,
        int year,
        String examName,
        List<String> tagNames,
        double accuracyRate,
        boolean solved
) {
}
