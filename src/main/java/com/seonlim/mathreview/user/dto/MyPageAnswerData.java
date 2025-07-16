package com.seonlim.mathreview.user.dto;

import com.seonlim.mathreview.user.entity.Answer;
import com.seonlim.mathreview.user.entity.AnswerStatus;

import java.time.LocalDateTime;

public record MyPageAnswerData(
        Long answerId,
        Long problemId,
        int likeCount,
        LocalDateTime submittedAt,
        AnswerStatus status
) {
    public static MyPageAnswerData from(Answer a) {
        return new MyPageAnswerData(
                a.getId(),
                a.getProblem().getId(),
                a.getLikeCount(),
                a.getSubmittedAt(),
                a.getStatus()
        );
    }
}
