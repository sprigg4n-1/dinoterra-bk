package com.dinoterra.security.favorite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteDinoRepository extends JpaRepository<FavoriteDino, Long> {
    List<FavoriteDino> findByUserId(Long userId);

    void deleteByUserIdAndDinoId(Long userId, Long dinoId);
}
