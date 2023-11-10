package com.example.usersservice.service;

import com.example.usersservice.model.KafkaFollowMessageDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaUserFollowerDetailsConsumerService {
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaUserFollowerDetailsConsumerService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "user-follow-topic", groupId = "user-group")
    public void receiveMessage(String message) {
        try {
            KafkaFollowMessageDetails details = objectMapper.readValue(message, KafkaFollowMessageDetails.class);
            log.info("Received Kafka message: {}", details);
        } catch (JsonProcessingException e) {
            log.error("Error processing received Kafka message", e);
        }
    }
}
