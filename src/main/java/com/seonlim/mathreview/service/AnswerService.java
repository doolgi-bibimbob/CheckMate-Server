package com.seonlim.mathreview.service;

import com.seonlim.mathreview.dto.AnswerDetail;
import com.seonlim.mathreview.dto.AnswerSubmit;
import com.seonlim.mathreview.entity.*;
import com.seonlim.mathreview.repository.ProblemRepository;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final ProblemRepository problemRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewRequestKafkaProducer reviewRequestKafkaProducer;

    @Transactional
    public void submitAnswer(AnswerSubmit request) {
        Problem problem = problemRepository.findById(request.problemId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "문제 ID가 유효하지 않습니다: " + request.problemId()));

        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = principal.getDomain();

        boolean isCorrect = Objects.equals(request.answer(), problem.getAnswer());
        AnswerStatus status = isCorrect ? AnswerStatus.CORRECT : AnswerStatus.INCORRECT;

        problem.recordSubmission(isCorrect);
        problemRepository.save(problem);

        Answer answer = Answer.builder()
                .user(user)
                .problem(problem)
                .answerImgSolutions(request.answerImgUrls())
                .status(status)
                .submittedAt(LocalDateTime.now())
                .build();
        answerRepository.save(answer);

        List<String> solutionUrls = Optional.ofNullable(problem.getProblemImgSolutions())
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);

        AnswerSubmit generated = AnswerSubmit.withGeneratedAnswerInfo(
                request,
                answer.getId(),
                problem.getProblemImageUrl(),
                solutionUrls
        );

        reviewRequestKafkaProducer.sendReviewRequestTest(generated);
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
