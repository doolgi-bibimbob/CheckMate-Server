package com.seonlim.mathreview.problem.dto;

import com.seonlim.mathreview.problem.entity.Problem;
import com.seonlim.mathreview.problem.entity.Tag;

import java.util.List;

public record ProblemDetail(
        Long id,
        Long examId,
        String title,
        String content,
        String solutionImageUrl,
        String problemImageUrl,
        List<String> problemImgSolutions,
        Long submissionCount,
        Long correctSubmissionCount,
        double accuracyRate,
        Long answer,
        List<TagSummary> tags
) {

    public static ProblemDetail from(Problem p) {
        return new ProblemDetail(
                p.getId(),
                p.getExam() != null ? p.getExam().getId() : null,
                p.getTitle(),
                p.getContent(),
                p.getSolutionImageUrl(),
                p.getProblemImageUrl(),
                List.copyOf(p.getProblemImgSolutions()),
                p.getSubmissionCount(),
                p.getCorrectSubmissionCount(),
                p.getAccuracyRate(),
                p.getAnswer(),
                p.getTags().stream()
                        .map(TagSummary::from)
                        .toList()
        );
    }

    public record TagSummary(Long id, String name) {
        static TagSummary from(Tag tag) {
            return new TagSummary(tag.getId(), tag.getName());
        }
    }
}

