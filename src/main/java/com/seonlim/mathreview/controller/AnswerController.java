package com.seonlim.mathreview.controller;

import com.seonlim.mathreview.dto.AnswerSubmitRequest;
import com.seonlim.mathreview.dto.AnswerSubmitRequestListTest;
import com.seonlim.mathreview.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/submit-test-list")
    public ResponseEntity<Void> submitAnswerListTest(@RequestBody AnswerSubmitRequestListTest request) {
        answerService.submitListTest(request);
        return ResponseEntity.ok().build();
    }
}
