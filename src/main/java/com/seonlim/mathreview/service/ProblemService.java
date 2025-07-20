package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.ProblemDetail;
import com.seonlim.mathreview.dto.ProblemFilterRequest;
import com.seonlim.mathreview.dto.ProblemFilterResponse;
import com.seonlim.mathreview.entity.Exam;
import com.seonlim.mathreview.entity.Problem;
import com.seonlim.mathreview.repository.ProblemRepository;
import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;

    public Page<ProblemFilterResponse> getFilteredProblems(ProblemFilterRequest request, Pageable pageable) {
        return problemRepository.filterProblems(
                request.title(),
                request.year(),
                request.tagName(),
                request.minAccuracyRate(),
                request.maxAccuracyRate(),
                pageable
        );
    }

    public Problem getProblemWithExamAndAnswers(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("❌ 문제 ID가 유효하지 않습니다: " + problemId));

        Exam exam = problem.getExam();

        List<Answer> answers = answerRepository.findByProblemId(problemId);

        return problem;
    }

    public ProblemDetail getProblemDetail(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("❌ 문제 ID가 유효하지 않습니다: " + problemId));

        List<Answer> latestAnswer = answerRepository.findByProblemId(problemId)
                .stream()
                .sorted((a1, a2) -> a2.getSubmittedAt().compareTo(a1.getSubmittedAt()))
                .toList();
        return ProblemDetail.from(problem, latestAnswer);
    }
}
