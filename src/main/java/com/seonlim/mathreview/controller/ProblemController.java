package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.ProblemDetail;
import com.seonlim.mathreview.dto.ProblemFilterRequest;
import com.seonlim.mathreview.dto.ProblemFilterResponse;
import com.seonlim.mathreview.service.ProblemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Problem", description = "문제 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/problem")
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "문제 필터 조회", description = "1번 / 문제 필터링을 위한 API")
    @PostMapping("/filter")
    public ResponseEntity<Page<ProblemFilterResponse>> filterProblems(
            @RequestBody ProblemFilterRequest request,
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<ProblemFilterResponse> result = problemService.getFilteredProblems(request, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/detail/{problemId}")
    public ResponseEntity<ProblemDetail> getProblemDetail(@PathVariable Long problemId) {
        ProblemDetail problemDetail = problemService.getProblemDetail(problemId);
        return ResponseEntity.ok(problemDetail);
    }
}
