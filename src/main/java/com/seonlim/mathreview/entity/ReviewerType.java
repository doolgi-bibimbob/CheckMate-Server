package com.seonlim.mathreview.entity;

import lombok.Getter;

@Getter
public enum ReviewerType {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    AI("AI");

    private final String type;

    ReviewerType(String type) {
        this.type = type;
    }

}
