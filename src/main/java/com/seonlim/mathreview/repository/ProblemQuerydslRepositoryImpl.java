package com.seonlim.mathreview.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seonlim.mathreview.dto.ProblemFilterResponse;
import com.seonlim.mathreview.entity.*;
import com.seonlim.mathreview.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        QExam exam = QExam.exam;
        QTag tag = QTag.tag;
        QAnswer answer = new QAnswer("answer");

        BooleanBuilder cond = new BooleanBuilder()
                .and(title != null ? problem.title.containsIgnoreCase(title) : null)
                .and(year != null ? exam.year.eq(year) : null)
                .and(tagName != null ? tag.name.containsIgnoreCase(tagName) : null)
                .and(minAccuracyRate != null
                        ? problem.correctSubmissionCount.multiply(100.0)
                        .divide(problem.submissionCount.coalesce(1L))
                        .goe(minAccuracyRate)
                        : null)
                .and(maxAccuracyRate != null
                        ? problem.correctSubmissionCount.multiply(100.0)
                        .divide(problem.submissionCount.coalesce(1L))
                        .loe(maxAccuracyRate)
                        : null);

        List<Problem> problems = queryFactory
                .selectFrom(problem)
                .leftJoin(problem.exam, exam).fetchJoin()
                .leftJoin(problem.tags, tag).fetchJoin()
                .distinct()
                .where(cond)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(problem.id.asc())
                .fetch();

        List<Long> problemIds = problems.stream()
                .map(Problem::getId)
                .toList();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<Long> correctSolvedProblemIds;

        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            Long userId = ((CustomUserDetails) auth.getPrincipal()).getDomain().getId();

            correctSolvedProblemIds = new HashSet<>(
                    queryFactory
                            .select(answer.problem.id)
                            .from(answer)
                            .where(answer.user.id.eq(userId)
                                    .and(answer.problem.id.in(problemIds))
                                    .and(answer.status.eq(AnswerStatus.CORRECT)))
                            .distinct()
                            .fetch()
            );
        } else {
            correctSolvedProblemIds = Collections.emptySet();
        }

        List<ProblemFilterResponse> content = problems.stream()
                .map(p -> new ProblemFilterResponse(
                        p.getId(),
                        p.getTitle(),
                        p.getExam().getYear(),
                        p.getExam().getName(),
                        p.getTags().stream().map(Tag::getName).toList(),
                        p.getAccuracyRate(),
                        correctSolvedProblemIds.contains(p.getId())
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
