package com.dinoterra.dinosaur.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class FavoriteDinoKafkaConsumer {

    @KafkaListener(topics = "dinosaurs-topic", groupId = "dinosaur-service")
    public void listen(Long dinosaurId) {
        System.out.println("Received dinosaur with ID: " + dinosaurId);
    }
}
