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

    public static ReviewerType fromUserType(UserType userType) {
        return switch (userType) {
            case STUDENT, HONOR_STUDENT -> STUDENT;
            case TEACHER, MENTOR -> TEACHER;
            case ADMIN, ANONYMOUS -> throw new IllegalArgumentException("해당 UserType은 리뷰어가 될 수 없습니다: " + userType);
        };
    }
}
