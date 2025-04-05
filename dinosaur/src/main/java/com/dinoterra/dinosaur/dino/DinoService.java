package com.dinoterra.dinosaur.dino;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;
import com.dinoterra.dinosaur.favorite.FavoriteDino;
import com.dinoterra.dinosaur.favorite.FavoriteRepository;
import com.dinoterra.dinosaur.image.ImageRepository;
import com.dinoterra.dinosaur.image.ImageResponse;
import com.dinoterra.dinosaur.location.FoundLocationRepository;
import com.dinoterra.dinosaur.location.FoundLocationResponse;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DinoService {
        private final DinoRepository dinoRepository;
        private final ImageRepository imageRepository;
        private final FoundLocationRepository locationRepository;
        private final FavoriteRepository favoriteRepository;

        public DinoResponse getDino(Long id) {
                Dino dino = dinoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Dino not found with ID " + id));

                List<ImageResponse> images = imageRepository.findByDino(dino)
                                .stream()
                                .map(image -> new ImageResponse(image.getId(), image.getImage(), image.getFileName(),
                                                dino.getId()))
                                .collect(Collectors.toList());

                List<FoundLocationResponse> locations = locationRepository.findByDino(dino)
                                .stream()
                                .map(location -> new FoundLocationResponse(location.getId(), location.getPlace(),
                                                location.getLatitude(), location.getLongitude(), dino.getId()))
                                .collect(Collectors.toList());

                return mapToDinoRes(dino, images, locations);
        }

        public Page<DinoResponse> getAllDinos(Pageable pageable, String name, String type, String diet, String period,
                        String placeLocation) {
                DinoType dinoType = (type != null) ? DinoType.valueOf(type) : null;
                DinoDiet dinoDiet = (diet != null) ? DinoDiet.valueOf(diet) : null;
                DinoPeriod dinoPeriod = (period != null) ? DinoPeriod.valueOf(period) : null;

                Specification<Dino> spec = DinoSpecification.filterBy(name, dinoType, dinoDiet, dinoPeriod,
                                placeLocation);

                return dinoRepository.findAll(spec, pageable).map(dino -> {
                        List<ImageResponse> images = imageRepository.findByDino(dino)
                                        .stream()
                                        .map(image -> new ImageResponse(image.getId(), image.getImage(),
                                                        image.getFileName(), dino.getId()))
                                        .collect(Collectors.toList());

                        List<FoundLocationResponse> locations = locationRepository.findByDino(dino)
                                        .stream()
                                        .map(location -> new FoundLocationResponse(location.getId(),
                                                        location.getPlace(),
                                                        location.getLatitude(), location.getLongitude(), dino.getId()))
                                        .collect(Collectors.toList());

                        return mapToDinoRes(dino, images, locations);
                });
        }

        public List<DinoResponse> getFiveRandomDinos() {
                List<Dino> allDinos = dinoRepository.findAll();
                Collections.shuffle(allDinos);
                return allDinos.stream().limit(5).map(dino -> {
                        List<ImageResponse> images = imageRepository.findByDino(dino)
                                        .stream()
                                        .map(image -> new ImageResponse(image.getId(), image.getImage(),
                                                        image.getFileName(), dino.getId()))
                                        .collect(Collectors.toList());

                        List<FoundLocationResponse> locations = locationRepository.findByDino(dino)
                                        .stream()
                                        .map(location -> new FoundLocationResponse(location.getId(),
                                                        location.getPlace(),
                                                        location.getLatitude(), location.getLongitude(), dino.getId()))
                                        .collect(Collectors.toList());

                        return mapToDinoRes(dino, images, locations);
                }).collect(Collectors.toList());
        }

        public DinoResponse createDino(DinoRequest dinoRequest) {
                var dino = mapToDinoReq(dinoRequest);
                dinoRepository.save(dino);

                return mapToDinoRes(dino, null, null);
        }

        public void changeDino(Long id, DinoRequest dinoRequest) {
                Dino dino = dinoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Dino not found with ID " + id));

                dino.setName(dinoRequest.name());
                dino.setLatinName(dinoRequest.latinName());
                dino.setDescription(dinoRequest.description());
                dino.setTypeOfDino(dinoRequest.typeOfDino());
                dino.setLength(dinoRequest.length());
                dino.setWeight(dinoRequest.weight());
                dino.setPeriod(dinoRequest.period());
                dino.setPeriodDate(dinoRequest.periodDate());
                dino.setPeriodDescription(dinoRequest.periodDescription());
                dino.setDiet(dinoRequest.diet());
                dino.setDietDescription(dinoRequest.dietDescription());

                dinoRepository.save(dino);
        }

        public void deleteDino(Long id) {
                dinoRepository.deleteById(id);
        }

        public List<DinoFavResponse> getFavoriteDinosByUserId(Long userId) {
                List<FavoriteDino> favoriteDinos = favoriteRepository.findByUserId(userId);

                return favoriteDinos.stream()
                                .map(favoriteDino -> mapToDinoFavRes(dinoRepository.findById(favoriteDino.getDinoId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Dino not found with ID " + favoriteDino.getDinoId())),
                                                null, null, favoriteDino.getId()))
                                .collect(Collectors.toList());
        }

        private Dino mapToDinoReq(DinoRequest dinoRequest) {
                Dino dino = new Dino();
                dino.setName(dinoRequest.name());
                dino.setLatinName(dinoRequest.latinName());
                dino.setDescription(dinoRequest.description());
                dino.setTypeOfDino(dinoRequest.typeOfDino());
                dino.setLength(dinoRequest.length());
                dino.setWeight(dinoRequest.weight());
                dino.setPeriod(dinoRequest.period());
                dino.setPeriodDate(dinoRequest.periodDate());
                dino.setPeriodDescription(dinoRequest.periodDescription());
                dino.setDiet(dinoRequest.diet());
                dino.setDietDescription(dinoRequest.dietDescription());
                return dino;
        }

        private DinoResponse mapToDinoRes(Dino dino, List<ImageResponse> images,
                        List<FoundLocationResponse> locations) {
                return new DinoResponse(dino.getId(), dino.getName(), dino.getLatinName(), dino.getDescription(),
                                dino.getTypeOfDino(), dino.getLength(), dino.getWeight(), dino.getPeriod(),
                                dino.getPeriodDate(),
                                dino.getPeriodDescription(), dino.getDiet(), dino.getDietDescription(), images,
                                locations);
        }

        private DinoFavResponse mapToDinoFavRes(Dino dino, List<ImageResponse> images,
                        List<FoundLocationResponse> locations, Long favId) {
                return new DinoFavResponse(dino.getId(), dino.getName(), dino.getLatinName(), dino.getDescription(),
                                dino.getTypeOfDino(), dino.getLength(), dino.getWeight(), dino.getPeriod(),
                                dino.getPeriodDate(),
                                dino.getPeriodDescription(), dino.getDiet(), dino.getDietDescription(), images,
                                locations, favId);
        }
}
