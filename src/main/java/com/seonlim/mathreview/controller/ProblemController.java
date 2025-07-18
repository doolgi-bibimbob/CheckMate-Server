package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.ProblemDetail;
import com.seonlim.mathreview.dto.ProblemFilterRequest;
import com.seonlim.mathreview.dto.ProblemFilterResponse;
import com.seonlim.mathreview.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/filter")
    public ResponseEntity<List<ProblemFilterResponse>> filterProblems(ProblemFilterRequest request) {
        List<ProblemFilterResponse> result = problemService.getFilteredProblems(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{problemId}")
    public ResponseEntity<ProblemDetail> getProblemDetail(@PathVariable Long problemId) {
        ProblemDetail problemDetail = problemService.getProblemDetail(problemId);
        return ResponseEntity.ok(problemDetail);
    }
}
