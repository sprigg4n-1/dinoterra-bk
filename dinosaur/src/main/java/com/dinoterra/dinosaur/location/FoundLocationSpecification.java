package com.dinoterra.dinosaur.location;

import org.springframework.data.jpa.domain.Specification;

import com.dinoterra.dinosaur.dino.Dino;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;

import jakarta.persistence.criteria.*;

public class FoundLocationSpecification {
    public static Specification<DinoFoundLocation> filterBy(String place, DinoPeriod period) {
        return (Root<DinoFoundLocation> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (place != null && !place.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("place")), "%" + place.toLowerCase() + "%"));
            }

            if (period != null) {
                Join<DinoFoundLocation, Dino> dinoJoin = root.join("dino");
                predicate = cb.and(predicate, cb.equal(dinoJoin.get("period"), period));
            }

            return predicate;
        };
    }
}
