package com.seonlim.mathreview.user.entity;

public enum ReviewerType {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    ADMIN("ADMIN");

    private final String type;

    ReviewerType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
