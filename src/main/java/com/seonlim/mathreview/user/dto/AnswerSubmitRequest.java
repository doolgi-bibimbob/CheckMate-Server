package com.seonlim.mathreview.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmitRequest {
    private Long userId;
    private Long problemId;
    private String answerImgUrl;
    private String solutionImgUrl;
}
