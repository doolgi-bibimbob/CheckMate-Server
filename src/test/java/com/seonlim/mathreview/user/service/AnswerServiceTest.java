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