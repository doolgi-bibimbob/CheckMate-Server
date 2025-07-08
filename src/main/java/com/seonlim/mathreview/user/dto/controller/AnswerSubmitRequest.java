package com.seonlim.mathreview.user.dto.controller;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class AnswerSubmitRequest {
    private Long userId;
    private Long problemId;
    private String answerImgUrl;
}
