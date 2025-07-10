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
    private Long answerId;
    private String problemImgUrl;
    private String answerImgUrl;
    private String solutionImgUrl;
    private Long answer;
}
