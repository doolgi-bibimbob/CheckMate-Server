package com.seonlim.mathreview.user.service;

import com.seonlim.mathreview.problem.entity.Problem;
import com.seonlim.mathreview.user.dto.MyPageAnswerData;
import com.seonlim.mathreview.user.dto.MyPageReviewData;
import com.seonlim.mathreview.user.dto.MyPageUserData;
import com.seonlim.mathreview.user.entity.Answer;
import com.seonlim.mathreview.user.entity.Review;
import com.seonlim.mathreview.user.entity.User;
import com.seonlim.mathreview.user.repository.AnswerRepository;
import com.seonlim.mathreview.user.repository.ReviewRepository;
import com.seonlim.mathreview.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AnswerRepository answerRepository;

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    UserService userService;

    @Test
    void getMyPageUserData_success() {
        // given
        User user = User.builder()
                .id(1L)
                .username("jun")
                .email("jun@test.com")
                .build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        MyPageUserData dto = userService.getMyPageUserData(1L);

        // then
        assertThat(dto.username()).isEqualTo("jun");
        assertThat(dto.email()).isEqualTo("jun@test.com");
        verify(userRepository).findById(1L);
    }

    @Test
    void getMyPageUserData_notFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getMyPageUserData(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void getMyPageAnswerData_success() {
        // given
        User user = User.builder().id(1L).username("jun").build();
        Problem problem = new Problem(); problem.setTitle("문제 1");
        Answer a1 = Answer.builder().id(10L).user(user).problem(problem)
                .submittedAt(LocalDateTime.now()).build();
        when(answerRepository.findAllByUserId(1L)).thenReturn(List.of(a1));

        // when
        List<MyPageAnswerData> list = userService.getMyPageAnswerData(1L);

        // then
        assertThat(list).hasSize(1);
        assertThat(list.get(0).problemTitle()).isEqualTo("문제 1");
    }

    @Test
    void getMyPageAnswerData_emptyThrows() {
        when(answerRepository.findAllByUserId(1L)).thenReturn(List.of());

        assertThatThrownBy(() -> userService.getMyPageAnswerData(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No answers found");
    }

    @Test
    void getMyPageReviewData_success() {
        // given
        User reviewer = User.builder().id(1L).username("jun").build();
        User target    = User.builder().id(2L).username("kim").build();
        Problem problem = new Problem(); problem.setTitle("문제 1");
        Answer answer = Answer.builder().id(10L).user(target).problem(problem).build();

        Review review = new Review();
        review.setAnswer(answer); review.setReviewer(reviewer);
        review.setCreatedAt(LocalDateTime.now());

        when(reviewRepository.findAllByReviewerId(1L)).thenReturn(List.of(review));

        // when
        List<MyPageReviewData> list = userService.getMyPageReviewData(1L);

        // then
        assertThat(list).hasSize(1);
        assertThat(list.getFirst().targetName()).isEqualTo("kim");
        assertThat(list.getFirst().problemTitle()).isEqualTo("문제 1");
    }

    @Test
    void getMyPageReviewData_noReviewsReturnsEmpty() {
        when(reviewRepository.findAllByReviewerId(2L)).thenReturn(List.of());

        List<MyPageReviewData> result = userService.getMyPageReviewData(2L);

        assertThat(result).isEmpty();
    }

}