package com.seonlim.mathreview.user.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class GptReviewResultConsumer {

    @KafkaListener(topics = "gpt-responses", groupId = "gpt-api-group")
    public void consume(String message) {

        System.out.println("Received message: " + message);
    }
}
