package com.seonlim.mathreview.user.controller;

import com.seonlim.mathreview.user.dto.controller.AnswerSubmitRequest;
import com.seonlim.mathreview.user.service.AnswerSubmitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerSubmitService answerSubmitService;

    @PostMapping
    public ResponseEntity<Void> submitAnswer(@RequestBody AnswerSubmitRequest request) {
        answerSubmitService.submit();
        return answerSubmitService.submitAnswer(answer);
    }
}
