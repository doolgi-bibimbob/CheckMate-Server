package com.seonlim.mathreview.problem.controller;

import com.seonlim.mathreview.problem.dto.ProblemFilterRequest;
import com.seonlim.mathreview.problem.dto.ProblemFilterResponse;
import com.seonlim.mathreview.problem.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problems")
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/filter")
    public ResponseEntity<List<ProblemFilterResponse>> filterProblems(ProblemFilterRequest request) {
        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
        return ResponseEntity.ok(result);
    }
}
