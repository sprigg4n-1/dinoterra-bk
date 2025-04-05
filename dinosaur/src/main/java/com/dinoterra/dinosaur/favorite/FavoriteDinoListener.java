package com.dinoterra.dinosaur.favorite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.dinoterra.dinosaur.events.FavoriteDinoEvent;
import com.dinoterra.dinosaur.events.FavoriteDinoRemoveEvent;

@Component
public class FavoriteDinoListener {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @KafkaListener(topics = "add-favorite-dinosaurs", groupId = "dino-service", containerFactory = "kafkaListenerContainerFactory")
    public void handleAddFavoriteEvent(FavoriteDinoEvent event) {
        favoriteRepository.save(new FavoriteDino(event.getUserId(), event.getDinoId()));
    }

    @KafkaListener(topics = "remove-favorite-dinosaurs", groupId = "dino-service", containerFactory = "kafkaListenerContainerFactoryRem")
    public void handleRemoveFavoriteEvent(FavoriteDinoRemoveEvent event) {
        favoriteRepository.deleteById(event.getFavDinoId());
    }
}
