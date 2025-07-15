package com.seonlim.mathreview.problem.service;

import com.seonlim.mathreview.problem.dto.ProblemFilterRequest;
import com.seonlim.mathreview.problem.dto.ProblemFilterResponse;
import com.seonlim.mathreview.problem.entity.Exam;
import com.seonlim.mathreview.problem.entity.Problem;
import com.seonlim.mathreview.problem.repository.ProblemQuerydslRepository;
import com.seonlim.mathreview.problem.repository.ProblemRepository;
import com.seonlim.mathreview.user.entity.Answer;
import com.seonlim.mathreview.user.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;

    public List<ProblemFilterResponse> getFilteredProblems(ProblemFilterRequest request) {
        return problemRepository.filterProblems(
                request.title(),
                request.year(),
                request.tagName(),
                request.minAccuracyRate()
        );
    }

    public Problem getProblemWithExamAndAnswers(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("❌ 문제 ID가 유효하지 않습니다: " + problemId));

        Exam exam = problem.getExam();

        List<Answer> answers = answerRepository.findByProblemId(problemId);

        return problem;
    }
}
