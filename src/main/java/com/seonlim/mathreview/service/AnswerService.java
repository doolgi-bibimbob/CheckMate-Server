package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.AnswerDetail;
import com.seonlim.mathreview.entity.*;
import com.seonlim.mathreview.repository.ProblemRepository;
import com.seonlim.mathreview.dto.AnswerSubmitRequest;
import com.seonlim.mathreview.dto.AnswerSubmitRequestListTest;
import com.seonlim.mathreview.kafka.producer.ReviewRequestKafkaProducer;
import com.seonlim.mathreview.repository.AnswerRepository;
import com.seonlim.mathreview.repository.ReviewRepository;
import com.seonlim.mathreview.repository.UserRepository;
import com.seonlim.mathreview.security.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewRequestKafkaProducer reviewRequestKafkaProducer;

    public void submit(AnswerSubmitRequest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException("문제 ID가 유효하지 않습니다: " + request.getProblemId()));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));

        Answer answer = Answer.builder()
                .user(user)
                .problem(problem)
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

        validateAnswer(request.getAnswer(), problem);

        reviewRequestKafkaProducer.sendReviewRequest(request);
    }

    public void validateAnswer(Long userAnswer, Problem problem) {
        boolean isCorrect = Objects.equals(userAnswer, problem.getAnswer());
        problem.recordSubmission(isCorrect);
        problemRepository.save(problem);
    }

    @Transactional
    public void submitListTest(AnswerSubmitRequestListTest request) {
        Problem problem = problemRepository.findById(request.getProblemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "문제 ID가 유효하지 않습니다: " + request.getProblemId()));

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = principal.getDomain();

        boolean isCorrect = Objects.equals(request.getAnswer(), problem.getAnswer());
        AnswerStatus status = isCorrect ? AnswerStatus.CORRECT : AnswerStatus.INCORRECT;

        problem.recordSubmission(isCorrect);
        problemRepository.save(problem);

        Answer answer = Answer.builder()
                .user(user)
                .problem(problem)
                .answerImgSolutions(request.getAnswerImgUrls())
                .status(status)
                .submittedAt(LocalDateTime.now())
                .build();
        answerRepository.save(answer);

        request.setAnswerId(answer.getId());
        request.setProblemImgUrl(problem.getProblemImageUrl());
        Optional.ofNullable(problem.getSolutionImageUrl())
                .ifPresentOrElse(
                        url -> request.setSolutionImgUrls(List.of(url)),
                        ()  -> request.setSolutionImgUrls(List.of())
                );

        reviewRequestKafkaProducer.sendReviewRequestTest(request);
    }

    public AnswerDetail getAnswerDetail(Long answerId) {
        Answer answer = answerRepository.findByIdFetch(answerId)
                .orElseThrow(() -> new EntityNotFoundException("answer not found: " + answerId));

        List<Review> reviews = reviewRepository.findByAnswerId(answerId);

        Review ai = null;
        List<AnswerDetail.UserReviewSummary> userSummaries = new ArrayList<>();

        for (Review r : reviews) {
            if (r.getReviewerType() == ReviewerType.AI) {
                ai = r;
            } else {
                userSummaries.add(AnswerDetail.UserReviewSummary.from(r));
            }
        }

        return AnswerDetail.of(
                answer,
                ai != null ? AnswerDetail.AiReview.from(ai) : null,
                userSummaries
        );
    }
}
