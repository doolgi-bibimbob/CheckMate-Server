package com.seonlim.mathreview.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seonlim.mathreview.problem.entity.Problem;
import com.seonlim.mathreview.problem.repository.ProblemRepository;
import com.seonlim.mathreview.user.dto.AnswerSubmitRequest;
import com.seonlim.mathreview.user.dto.GptReviewRequest;
import com.seonlim.mathreview.user.entity.Answer;
import com.seonlim.mathreview.user.entity.AnswerStatus;
import com.seonlim.mathreview.user.entity.User;
import com.seonlim.mathreview.user.kafka.ReviewKafkaProducer;
import com.seonlim.mathreview.user.repository.AnswerRepository;
import com.seonlim.mathreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerSubmitService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final ReviewKafkaProducer reviewKafkaProducer;

    public void submit(AnswerSubmitRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new NoSuchBeanDefinitionException(request.getProblemId().getClass()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));

        Answer answer = Answer.builder()
                .user(user)
                .problemId(request.getProblemId())
                .imgSolution(request.getAnswerImgUrl())
                .status(AnswerStatus.PENDING_REVIEW)
                .submittedAt(LocalDateTime.now())
                .build();

        answerRepository.save(answer);

        GptReviewRequest payload = new GptReviewRequest(
                request.getAnswerImgUrl(),
                problem.getSolutionImageUrls()
        );

        reviewKafkaProducer.sendReviewRequest(payload);
    }
}
