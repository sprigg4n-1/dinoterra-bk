package com.dinoterra.dinosaur.dino;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DinoRepository extends JpaRepository<Dino, Long>, JpaSpecificationExecutor<Dino> {
}
