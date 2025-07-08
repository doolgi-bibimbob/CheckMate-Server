package com.seonlim.mathreview.user.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seonlim.mathreview.user.dto.kafka.GptReviewRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendReviewRequest(GptReviewRequest payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("gpt-review-request", json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize review request", e);
        }
    }

}
