package com.seonlim.mathreview.problem.service;

import com.seonlim.mathreview.problem.dto.ProblemFilterRequest;
import com.seonlim.mathreview.problem.dto.ProblemFilterResponse;
import com.seonlim.mathreview.problem.repository.ProblemQuerydslRepository;
import com.seonlim.mathreview.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemRepository problemRepository;

    public List<ProblemFilterResponse> getFilteredProblems(ProblemFilterRequest request) {
        return problemRepository.filterProblems(
                request.title(),
                request.year(),
                request.tagName(),
                request.minAccuracyRate()
        );
    }
}
