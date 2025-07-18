package com.seonlim.mathreview.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seonlim.mathreview.dto.ProblemFilterResponse;
import com.seonlim.mathreview.entity.QExam;
import com.seonlim.mathreview.entity.QProblem;
import com.seonlim.mathreview.entity.QTag;
import com.seonlim.mathreview.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProblemQuerydslRepositoryImpl implements ProblemQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProblemFilterResponse> filterProblems(String title,
                                                      Integer year,
                                                      String tagName,
                                                      Double minAccuracyRate,
                                                      Double maxAccuracyRate,
                                                      Pageable pageable) {

        QProblem problem = QProblem.problem;
        QExam exam       = QExam.exam;
        QTag tag         = QTag.tag;

        BooleanBuilder cond = new BooleanBuilder()
                .and(title   != null ? problem.title.containsIgnoreCase(title) : null)
                .and(year    != null ? exam.year.eq(year)                     : null)
                .and(tagName != null ? tag.name.containsIgnoreCase(tagName)   : null)
                .and(minAccuracyRate != null
                        ? problem.correctSubmissionCount.multiply(100.0)
                        .divide(problem.submissionCount).goe(minAccuracyRate)
                        : null)
                .and(maxAccuracyRate != null
                        ? problem.correctSubmissionCount.multiply(100.0)
                        .divide(problem.submissionCount).loe(maxAccuracyRate)
                        : null);

        List<ProblemFilterResponse> content = queryFactory
                .selectFrom(problem)
                .leftJoin(problem.exam, exam).fetchJoin()
                .leftJoin(problem.tags, tag).fetchJoin()
                .distinct()
                .where(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(problem.id.desc())
                .fetch()
                .stream()
                .map(p -> new ProblemFilterResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getExam().getYear(),
                        p.getExam().getName(),
                        p.getTags().stream().map(Tag::getName).toList(),
                        p.getAccuracyRate()
                ))
                .toList();

        Long totalObj = queryFactory
                .select(problem.id.countDistinct())
                .from(problem)
                .leftJoin(problem.exam, exam)
                .leftJoin(problem.tags, tag)
                .where(cond)
                .fetchOne();

        long total = totalObj != null ? totalObj : 0L;

        return new PageImpl<>(content, pageable, total);
    }

}
