package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.entity.AnswerStatus;

import java.time.LocalDateTime;

public record MyPageAnswerData(
        Long answerId,
        String problemTitle,
        int likeCount,
        LocalDateTime submittedAt,
        AnswerStatus status
) {
    public static MyPageAnswerData from(Answer a) {
        return new MyPageAnswerData(
                a.getId(),
                a.getProblem().getTitle(),
                a.getLikeCount(),
                a.getSubmittedAt(),
                a.getStatus()
        );
    }
}
