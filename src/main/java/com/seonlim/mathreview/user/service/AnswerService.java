package com.seonlim.mathreview.user.service;

import com.seonlim.mathreview.problem.entity.Problem;
import com.seonlim.mathreview.problem.repository.ProblemRepository;
import com.seonlim.mathreview.user.dto.AnswerSubmitRequest;
import com.seonlim.mathreview.user.entity.Answer;
import com.seonlim.mathreview.user.entity.AnswerStatus;
import com.seonlim.mathreview.user.entity.User;
import com.seonlim.mathreview.user.kafka.producer.ReviewRequestKafkaProducer;
import com.seonlim.mathreview.user.repository.AnswerRepository;
import com.seonlim.mathreview.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final ReviewRequestKafkaProducer reviewRequestKafkaProducer;

    public void submit(AnswerSubmitRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("문제 ID가 유효하지 않습니다: " + request.getProblemId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));

        Answer answer = Answer.builder()
                .user(user)
                .problemId(problem.getId())
                .imgSolution(request.getAnswerImgUrl())
                .status(AnswerStatus.PENDING_REVIEW)
                .submittedAt(LocalDateTime.now())
                .build();

        answerRepository.save(answer);

        Optional.ofNullable(answer.getId())
                .ifPresent(request::setAnswerId);

        Optional.ofNullable(problem.getProblemImageUrl())
                .ifPresent(request::setProblemImgUrl);

        Optional.ofNullable(problem.getSolutionImageUrl())
                .ifPresent(request::setSolutionImgUrl);

        Optional.ofNullable(request.getAnswerImgUrl())
                .ifPresent(request::setAnswerImgUrl);

        reviewRequestKafkaProducer.sendReviewRequest(request);
    }
}
