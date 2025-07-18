package com.seonlim.mathreview.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seonlim.mathreview.entity.Answer;
import com.seonlim.mathreview.entity.AnswerStatus;
import com.seonlim.mathreview.entity.Review;
import com.seonlim.mathreview.entity.ReviewerType;
import com.seonlim.mathreview.kafka.dto.GptReviewResponse;
import com.seonlim.mathreview.repository.AnswerRepository;
import com.seonlim.mathreview.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GptReviewResultConsumer {

    private final ObjectMapper objectMapper;
    private final AnswerRepository answerRepository;
    private final ReviewRepository reviewRepository;

    @KafkaListener(topics = "gpt-review-results", groupId = "review-result-consumer")
    public void consume(String message) {
        Optional.ofNullable(message)
                .map(msg -> {
                    try {
                        return objectMapper.readValue(msg, GptReviewResponse.class);
                    } catch (IOException e) {
                        throw new RuntimeException("❌ JSON 파싱 실패: " + e.getMessage(), e);
                    }
                })
                .ifPresentOrElse(response -> {
                    log.info("✅ Gpt Review Result: {}", response);
                    Answer answer = answerRepository.findById(response.getAnswerId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 answerId를 찾을 수 없습니다: " + response.getAnswerId()));

                    answer.setStatus(AnswerStatus.REVIEWED);
                    answerRepository.save(answer);

                    Review review = Review.builder()
                            .answer(answer)
                            .reviewer(null)
                            .reviewerType(ReviewerType.AI)
                            .content(response.getReviewResult())
                            .rating(0)
                            .createdAt(LocalDateTime.now())
                            .build();

                    reviewRepository.save(review);

                    log.info("✅ Review 저장 완료: {}", review);
                }, () -> {
                    log.error("❌ 메시지가 비어있거나 null입니다.");
                });
    }

    @KafkaListener(topics = "gpt-review-results-test", groupId = "review-result-consumer")
    public void consumeTest(String message) {
        Optional.ofNullable(message)
                .map(msg -> {
                    try {
                        return objectMapper.readValue(msg, GptReviewResponse.class);
                    } catch (IOException e) {
                        throw new RuntimeException("❌ JSON 파싱 실패: " + e.getMessage(), e);
                    }
                })
                .ifPresentOrElse(response -> {
                    log.info("✅ Gpt Review Result: {}", response);
                    Answer answer = answerRepository.findById(response.getAnswerId())
                            .orElseThrow(() -> new IllegalArgumentException("해당 answerId를 찾을 수 없습니다: " + response.getAnswerId()));

                    answer.setStatus(AnswerStatus.REVIEWED);
                    answerRepository.save(answer);

                    Review review = Review.builder()
                            .answer(answer)
                            .reviewer(null)
                            .reviewerType(ReviewerType.AI)
                            .content(response.getReviewResult())
                            .rating(0)
                            .createdAt(LocalDateTime.now())
                            .build();

                    reviewRepository.save(review);

                    log.info("✅ Review 저장 완료: {}", review);
                }, () -> {
                    log.error("❌ 메시지가 비어있거나 null입니다.");
                });
    }

}
