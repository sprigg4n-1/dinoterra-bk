package com.dinoterra.security.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DinoConsumer {

    @KafkaListener(topics = "dinosaurs-topic", groupId = "user-service")
    public void listen(Long dinosaurId) {
        System.out.println("Received Dinosaur with ID: " + dinosaurId);
    }
}
