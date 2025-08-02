package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.entity.AnswerStatus;

import java.time.LocalDateTime;

public record AnswerSummary(
        Long id,
//        String content,
        Long userId,
        String username,
        AnswerStatus answerStatus,
        LocalDateTime submittedAt
) {
    static AnswerSummary from(Answer a) {
        return new AnswerSummary(
                a.getId(),
                a.getUser().getId(),
                a.getUser().getUsername(),
                a.getStatus(),
                a.getSubmittedAt()
        );
    }
}

