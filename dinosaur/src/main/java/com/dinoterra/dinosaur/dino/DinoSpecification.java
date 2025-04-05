package com.dinoterra.dinosaur.dino;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;
import com.dinoterra.dinosaur.location.DinoFoundLocation;

public class DinoSpecification {
    public static Specification<Dino> filterBy(String name, DinoType type, DinoDiet diet, DinoPeriod period, String placeLocation) {
        return (Root<Dino> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null && !name.isEmpty()) {
                predicate = cb.and(predicate, cb.or(
                    cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("latinName")), "%" + name.toLowerCase() + "%")
                ));
            }
            if (type != null) {
                predicate = cb.and(predicate, cb.equal(root.get("typeOfDino"), type));
            }
            if (diet != null) {
                predicate = cb.and(predicate, cb.equal(root.get("diet"), diet));
            }
            if (period != null) {
                predicate = cb.and(predicate, cb.equal(root.get("period"), period));
            }
            if (placeLocation != null && !placeLocation.isEmpty()) {
                Join<Dino, DinoFoundLocation> locationJoin = root.join("foundLocations", JoinType.LEFT);
                predicate = cb.and(predicate, cb.like(cb.lower(locationJoin.get("place")), "%" + placeLocation.toLowerCase() + "%"));
            }

            return predicate;
        };
    }
}
