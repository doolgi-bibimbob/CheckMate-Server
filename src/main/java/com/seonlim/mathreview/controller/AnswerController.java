package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.AnswerDetail;
import com.seonlim.mathreview.dto.AnswerSubmit;
import com.seonlim.mathreview.dto.AnswerSubmitRequest;
import com.seonlim.mathreview.dto.AnswerSubmitRequestListTest;
import com.seonlim.mathreview.service.AnswerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping("/submit-test")
    public ResponseEntity<Void> submitAnswer(@RequestBody AnswerSubmitRequest request) {
        answerService.submit(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/submit")
    public ResponseEntity<Void> submitAnswerListTest(@RequestBody AnswerSubmit request) {
        answerService.submitAnswer(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/detail/{answerId}")
    public ResponseEntity<AnswerDetail> getAnswerDetail(@PathVariable Long answerId) {
        AnswerDetail answerDetail = answerService.getAnswerDetail(answerId);
        return ResponseEntity.ok(answerDetail);
    }
}
