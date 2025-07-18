package com.seonlim.mathreview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerSubmitRequestListTest {
    private Long userId;
    private Long problemId;
    private Long answerId;
    private String problemImgUrl;
    private List<String> answerImgUrls;
    private List<String> solutionImgUrls;
    private Long answer;
}
