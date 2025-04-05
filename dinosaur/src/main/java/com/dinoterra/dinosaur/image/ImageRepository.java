package com.dinoterra.dinosaur.image;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dinoterra.dinosaur.dino.Dino;

public interface ImageRepository extends JpaRepository<DinoImage, Long> {
    List<DinoImage> findByDino(Dino dino);
}
