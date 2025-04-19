package com.dinoterra.security.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class DinoProducer {
    private final KafkaTemplate<String, Long> kafkaTemplate;

    @Autowired
    public DinoProducer(KafkaTemplate<String, Long> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDinosaur(Long dinosaurId) {
        kafkaTemplate.send("dinosaurs-topic", String.valueOf(dinosaurId), dinosaurId);
    }
}
