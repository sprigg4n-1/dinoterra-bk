package com.dinoterra.security.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dinoterra.security.kafka.DinoProducer;
import com.dinoterra.security.user.User;
import com.dinoterra.security.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteDinoService {
    private final FavoriteDinoRepository favoriteDinoRepository;
    private final UserRepository userRepository;
    private final DinoProducer dinosaurProducer;

    public void addFavorite(Long userId, Long dinosaurId) {
        FavoriteDino favoriteDino = new FavoriteDino();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with ID " + userId));

        favoriteDino.setUser(user);
        favoriteDino.setDinoId(dinosaurId);

        favoriteDinoRepository.save(favoriteDino);

        dinosaurProducer.sendDinosaur(dinosaurId);
    }

    @Transactional
    public void removeFavorite(Long userId, Long dinosaurId) {
        favoriteDinoRepository.deleteByUserIdAndDinoId(userId, dinosaurId);
    }

    public List<FavoriteDino> getFavorites(Long userId) {
        return favoriteDinoRepository.findByUserId(userId);
    }
}