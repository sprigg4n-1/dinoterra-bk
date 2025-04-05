package com.dinoterra.dinosaur.favorite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteDino, Long> {
    List<FavoriteDino> findByUserId(Long userId);
}
