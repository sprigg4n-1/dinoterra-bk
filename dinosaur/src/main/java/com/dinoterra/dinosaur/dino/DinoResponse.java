package com.dinoterra.dinosaur.dino;

import java.util.List;

import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;
import com.dinoterra.dinosaur.image.ImageResponse;
import com.dinoterra.dinosaur.location.FoundLocationResponse;

public record DinoResponse(Long id, String name, String latinName, String description, DinoType typeOfDino, Double length, Integer weight, DinoPeriod period, String periodDate, String periodDescription, DinoDiet diet, String dietDescription, List<ImageResponse> images, List<FoundLocationResponse> foundLocations) {
    
}
