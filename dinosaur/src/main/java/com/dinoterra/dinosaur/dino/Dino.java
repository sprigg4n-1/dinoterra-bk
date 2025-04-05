package com.dinoterra.dinosaur.dino;


import java.util.ArrayList;
import java.util.List;

import com.dinoterra.dinosaur.dino.enums.*;
import com.dinoterra.dinosaur.image.DinoImage;
import com.dinoterra.dinosaur.location.DinoFoundLocation;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "dinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Dino {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    // common
    private String name;
    private String latinName;
    @Column(length = 10000)
    private String description;

    @Enumerated(EnumType.STRING)
    private DinoType typeOfDino;

    private Double length;
    private Integer weight;
    
    // period
    @Enumerated(EnumType.STRING)
    private DinoPeriod period;
    private String periodDate;
    @Column(length = 10000)
    private String periodDescription;

    // diet
    @Enumerated(EnumType.STRING)
    private DinoDiet diet;
    @Column(length = 10000)
    private String dietDescription;

    @OneToMany(mappedBy = "dino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DinoFoundLocation> foundLocations = new ArrayList<>();

    @OneToMany(mappedBy = "dino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DinoImage> images = new ArrayList<>();
}
