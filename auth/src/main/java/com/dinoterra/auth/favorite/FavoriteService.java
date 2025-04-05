package com.dinoterra.auth.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.dinoterra.auth.events.FavoriteDinoEvent;
import com.dinoterra.auth.events.FavoriteDinoRemoveEvent;

@Service
public class FavoriteService {
    private final KafkaTemplate<String, FavoriteDinoEvent> kafkaTemplate;
    private final KafkaTemplate<String, FavoriteDinoRemoveEvent> kafkaTemplateDel;

    public FavoriteService(
            KafkaTemplate<String, FavoriteDinoEvent> kafkaTemplate,
            KafkaTemplate<String, FavoriteDinoRemoveEvent> kafkaTemplateDel) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateDel = kafkaTemplateDel;
    }

    public void addFavorite(Long userId, Long dinoId) {
        FavoriteDinoEvent event = new FavoriteDinoEvent(userId, dinoId);
        kafkaTemplate.send("add-favorite-dinosaurs", event);
    }

    public void removeFavorite(Long favDinoId) {
        FavoriteDinoRemoveEvent event = new FavoriteDinoRemoveEvent(favDinoId);
        kafkaTemplateDel.send("remove-favorite-dinosaurs", event);
    }
}
