package com.dinoterra.dinosaur.location;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dinoterra.dinosaur.dino.Dino;
import com.dinoterra.dinosaur.dino.DinoRepository;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FoundLocationService {
    private final FoundLocationRepository locationRepository;
    private final DinoRepository dinoRepository;

    public List<FoundLocationResponse> getAllLocations(String place, String period) {

        DinoPeriod dinoPeriod = (period != null) ? DinoPeriod.valueOf(period) : null;
        Specification<DinoFoundLocation> spec = FoundLocationSpecification.filterBy(place, dinoPeriod);

        List<DinoFoundLocation> locations = locationRepository.findAll(spec);
        return locations.stream().map(this::mapToLocationRes).toList();
    }

    public List<FoundLocationResponse> getLocationsByDinoId(Long dinoId) {
        Dino dino = dinoRepository.findById(dinoId)
                .orElseThrow(() -> new RuntimeException("Dino not found with ID " + dinoId));

        List<DinoFoundLocation> locations = locationRepository.findByDino(dino);

        return locations.stream().map(this::mapToLocationRes).toList();
    }

    public void createLocation(FoundLocationRequest locationRequest) {
        var location = mapToLocationEntity(locationRequest);
        locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    private DinoFoundLocation mapToLocationEntity(FoundLocationRequest locationRequest) {
        Dino dino = dinoRepository.findById(locationRequest.dino_id())
                .orElseThrow(() -> new RuntimeException("Dino not found with ID " + locationRequest.dino_id()));

        DinoFoundLocation location = new DinoFoundLocation();
        location.setPlace(locationRequest.place());
        location.setLatitude(locationRequest.latitude());
        location.setLongitude(locationRequest.longitude());
        location.setDino(dino);
        return location;
    }

    private FoundLocationResponse mapToLocationRes(DinoFoundLocation dinoLocation) {
        return new FoundLocationResponse(
                dinoLocation.getId(),
                dinoLocation.getPlace(),
                dinoLocation.getLatitude(),
                dinoLocation.getLongitude(),
                dinoLocation.getDino().getId());
    }
}
