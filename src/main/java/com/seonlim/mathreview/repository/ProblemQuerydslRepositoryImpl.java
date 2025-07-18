package com.seonlim.mathreview.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seonlim.mathreview.dto.ProblemFilterResponse;
import com.seonlim.mathreview.problem.entity.QExam;
import com.seonlim.mathreview.problem.entity.QProblem;
import com.seonlim.mathreview.problem.entity.QTag;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProblemQuerydslRepositoryImpl implements ProblemQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProblemFilterResponse> filterProblems(String title, Integer year, String tagName, Double minAccuracyRate) {
        QProblem problem = QProblem.problem;
        QExam exam = QExam.exam;
        QTag tag = QTag.tag;

        return queryFactory
                .selectFrom(problem)
                .leftJoin(problem.exam, exam).fetchJoin()
                .leftJoin(problem.tags, tag).fetchJoin()
                .distinct()
                .where(
                        title != null ? problem.title.containsIgnoreCase(title) : null,
                        year != null ? exam.year.eq(year) : null,
                        tagName != null ? tag.name.containsIgnoreCase(tagName) : null,
                        minAccuracyRate != null
                                ? problem.correctSubmissionCount.multiply(100.0)
                                .divide(problem.submissionCount).goe(minAccuracyRate)
                                : null
                )
                .fetch()
                .stream()
                .map(p -> new ProblemFilterResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getExam().getYear(),
                        p.getExam().getName(),
                        p.getTags().stream().map(tagEntity -> tagEntity.getName()).toList(),
                        p.getAccuracyRate()
                ))
                .toList();
    }
}
