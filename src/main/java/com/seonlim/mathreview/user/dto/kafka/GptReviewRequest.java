package com.seonlim.mathreview.user.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptReviewRequest {
    private String answerImgUrl;
    private List<String> solutionImgUrl;
}
