package com.dinoterra.dinosaur.dino;

import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;

public record DinoRequest(String name, String latinName, String description, DinoType typeOfDino, Double length, Integer weight, DinoPeriod period, String periodDate, String periodDescription, DinoDiet diet, String dietDescription) {
    
}
