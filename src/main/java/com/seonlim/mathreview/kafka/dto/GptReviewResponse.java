package com.seonlim.mathreview.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GptReviewResponse {
    private Long userId;
    private Long problemId;
    private Long answerId;
    private String ReviewResult;
}

