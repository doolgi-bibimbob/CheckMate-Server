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

    public AnswerSubmitRequest(long l, long l1, Object o, String image, long l2) {
        this.userId = l;
        this.problemId = l1;
        this.answerId = null; // Assuming answerId is not provided in this constructor
        this.problemImgUrl = image;
        this.answerImgUrl = null; // Assuming answerImgUrl is not provided in this constructor
        this.solutionImgUrl = null; // Assuming solutionImgUrl is not provided in this constructor
        this.answer = l2;
    }
}
