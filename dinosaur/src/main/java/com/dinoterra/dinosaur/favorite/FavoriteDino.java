package com.dinoterra.dinosaur.favorite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favorites_dinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long dinoId;

    public FavoriteDino(Long userId, Long dinoId) {
        this.userId = userId;
        this.dinoId = dinoId;
    }
}
