package com.seonlim.mathreview.dto;

import java.util.List;

public record AnswerSubmit(
        Long problemId,
        Long answerId,
        String problemImgUrl,
        List<String> answerImgUrls,
        List<String> solutionImgUrls,
        Long answer
) {
    public static AnswerSubmit withGeneratedAnswerInfo(
            AnswerSubmit original,
            Long answerId,
            String problemImgUrl,
            List<String> solutionImgUrls
    ) {
        return new AnswerSubmit(
                original.problemId(),
                answerId,
                problemImgUrl,
                original.answerImgUrls(),
                solutionImgUrls,
                original.answer()
        );
    }
}
