package com.seonlim.mathreview.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seonlim.mathreview.dto.AnswerSubmit;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewRequestKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendReviewRequestTest(AnswerSubmit request) {
        try {
            String json = objectMapper.writeValueAsString(request);
            kafkaTemplate.send("gpt-review-requests-test", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize review request", e);
        }
    }
}
