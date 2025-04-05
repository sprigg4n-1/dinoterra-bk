package com.dinoterra.dinosaur.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.dinoterra.dinosaur.dino.Dino;

public interface FoundLocationRepository
        extends JpaRepository<DinoFoundLocation, Long>, JpaSpecificationExecutor<DinoFoundLocation> {
    List<DinoFoundLocation> findByDino(Dino dino);
}
