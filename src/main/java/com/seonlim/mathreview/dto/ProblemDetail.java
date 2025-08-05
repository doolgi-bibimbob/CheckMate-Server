package com.seonlim.mathreview.dto;

import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.entity.Problem;
import com.seonlim.mathreview.entity.Tag;

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
        Long year,
        List<TagSummary> tags,
        List<AnswerSummary> answers
) {

    public static ProblemDetail from(Problem p, List<Answer> answers) {
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
                (long) p.getExam().getYear(),
                p.getTags().stream()
                        .map(TagSummary::from)
                        .toList(),
                answers.stream()
                        .map(AnswerSummary::from)
                        .toList()
        );
    }

    public record TagSummary(Long id, String name) {
        static TagSummary from(Tag tag) {
            return new TagSummary(tag.getId(), tag.getName());
        }
    }
}

