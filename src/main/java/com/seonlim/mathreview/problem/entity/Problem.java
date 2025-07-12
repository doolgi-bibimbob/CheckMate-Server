package com.seonlim.mathreview.problem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

    @ElementCollection
    @CollectionTable(name = "problem_img_solutions", joinColumns = @JoinColumn(name = "problem_id"))
    private List<String> problemImgSolutions = new ArrayList<>();

    private String content;

    private Long submissionCount = 0L;

    private Long correctSubmissionCount = 0L;

    private Long answer;

    private String title;

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
