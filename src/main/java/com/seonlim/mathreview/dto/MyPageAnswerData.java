package com.seonlim.mathreview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.entity.AnswerStatus;

import java.time.LocalDateTime;

public record MyPageAnswerData(
        Long answerId,
        String problemTitle,
        int likeCount,

        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'",
                timezone = "UTC"
        )
        LocalDateTime submittedAt,

        AnswerStatus status,
        Long problemId
) {
    public static MyPageAnswerData from(Answer a) {
        return new MyPageAnswerData(
                a.getId(),
                a.getProblem().getTitle(),
                a.getLikeCount(),
                a.getSubmittedAt(),
                a.getStatus(),
                a.getProblem().getId()
        );
    }
}
