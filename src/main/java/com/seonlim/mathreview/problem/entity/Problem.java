package com.seonlim.mathreview.problem.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Problem {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @ManyToMany
    @JoinTable(
            name = "problem_tag",
            joinColumns = @JoinColumn(name = "problem_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    private String solutionImageUrl;

    private String problemImageUrl;

    private String content;

    private Long submissionCount = 0L;

    private Long correctSubmissionCount = 0L;

    private Long answer;

    public double getAccuracyRate() {
        return submissionCount > 0
                ? (correctSubmissionCount * 100.0) / submissionCount
                : 0.0;
    }

    public void recordSubmission(boolean isCorrect) {
        submissionCount++;
        if (isCorrect) correctSubmissionCount++;
    }
}
