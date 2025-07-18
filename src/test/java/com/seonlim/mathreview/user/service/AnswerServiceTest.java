package com.seonlim.mathreview.user.service;

import com.seonlim.mathreview.entity.Problem;
import com.seonlim.mathreview.repository.ProblemRepository;
import com.seonlim.mathreview.dto.AnswerSubmitRequestListTest;
import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.entity.User;
import com.seonlim.mathreview.kafka.producer.ReviewRequestKafkaProducer;
import com.seonlim.mathreview.repository.AnswerRepository;
import com.seonlim.mathreview.repository.UserRepository;
import com.seonlim.mathreview.service.AnswerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {
    @InjectMocks
    private AnswerService answerService;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private ReviewRequestKafkaProducer reviewRequestKafkaProducer;

    private Problem dummyProblem;
    private User dummyUser;

    @BeforeEach
    void setup() {
        dummyUser = new User();
        dummyUser.setId(1L);

        dummyProblem = new Problem();
        dummyProblem.setId(1L);
        dummyProblem.setAnswer(5L);
        dummyProblem.setProblemImageUrl("problem.png");
        dummyProblem.setSolutionImageUrl("solution.png");
        dummyProblem.setSubmissionCount(0L);
        dummyProblem.setCorrectSubmissionCount(0L);
    }

    @Test
    void 정상제출_이미지리스트_카프카송신() {
        AnswerSubmitRequestListTest request = new AnswerSubmitRequestListTest(
                1L, 1L, null, null,
                List.of("img1.png", "img2.png"), List.of(), 5L
        );

        when(problemRepository.findById(1L)).thenReturn(Optional.of(dummyProblem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        when(answerRepository.save(any())).thenAnswer(invocation -> {
            Answer saved = invocation.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        answerService.submitListTest(request);

        assertEquals(100L, request.getAnswerId());
        assertEquals("problem.png", request.getProblemImgUrl());
        assertEquals(List.of("solution.png"), request.getSolutionImgUrls());
        assertEquals(List.of("img1.png", "img2.png"), request.getAnswerImgUrls());

        verify(answerRepository).save(any());
        verify(problemRepository).save(dummyProblem);
        verify(reviewRequestKafkaProducer).sendReviewRequestTest(request);
    }

    @Test
    void 문제없음_예외발생() {
        AnswerSubmitRequestListTest request = new AnswerSubmitRequestListTest(1L, 999L, null, null, List.of(), List.of(), 5L);
        when(problemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> answerService.submitListTest(request));
    }

    @Test
    void 유저없음_예외발생() {
        AnswerSubmitRequestListTest request = new AnswerSubmitRequestListTest(999L, 1L, null, null, List.of(), List.of(), 5L);
        when(problemRepository.findById(1L)).thenReturn(Optional.of(dummyProblem));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> answerService.submitListTest(request));
    }

    @Test
    void 솔루션이미지_NULL일때_빈리스트설정됨() {
        dummyProblem.setSolutionImageUrl(null);

        AnswerSubmitRequestListTest request = new AnswerSubmitRequestListTest(
                1L, 1L, null, null, List.of("img.png"), List.of(), 5L
        );

        when(problemRepository.findById(1L)).thenReturn(Optional.of(dummyProblem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        when(answerRepository.save(any())).thenReturn(new Answer());

        answerService.submitListTest(request);

        assertEquals(List.of(), request.getSolutionImgUrls());
    }

    @Test
    void 정답률_계산_검증() {
        dummyProblem.setSubmissionCount(0L);
        dummyProblem.setCorrectSubmissionCount(0L);
        dummyProblem.setAnswer(5L);

        AnswerSubmitRequestListTest request = new AnswerSubmitRequestListTest(
                1L, 1L, null, null, List.of("img.png"), List.of(), 5L
        );

        when(problemRepository.findById(1L)).thenReturn(Optional.of(dummyProblem));
        when(userRepository.findById(1L)).thenReturn(Optional.of(dummyUser));
        when(answerRepository.save(any())).thenReturn(new Answer());

        answerService.submitListTest(request);

        assertEquals(1L, dummyProblem.getSubmissionCount());
        assertEquals(1L, dummyProblem.getCorrectSubmissionCount());
    }

    @Test
    void validateAnswer_정답인경우_submission과_correctSubmission_증가() {
        // when
        answerService.validateAnswer(5L, dummyProblem);

        // then
        assertEquals(1L, dummyProblem.getSubmissionCount());
        assertEquals(1L, dummyProblem.getCorrectSubmissionCount());

        verify(problemRepository).save(dummyProblem);
    }

    @Test
    void validateAnswer_오답인경우_submission만_증가() {
        // when
        answerService.validateAnswer(4L, dummyProblem);

        // then
        assertEquals(1L, dummyProblem.getSubmissionCount());
        assertEquals(0L, dummyProblem.getCorrectSubmissionCount());

        verify(problemRepository).save(dummyProblem);
    }
}