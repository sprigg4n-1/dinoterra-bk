package com.dinoterra.dinosaur;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dinoterra.dinosaur.dino.DinoRequest;
import com.dinoterra.dinosaur.dino.DinoResponse;
import com.dinoterra.dinosaur.dino.DinoService;
import com.dinoterra.dinosaur.image.ImageRequest;
import com.dinoterra.dinosaur.image.ImageResponse;
import com.dinoterra.dinosaur.image.ImageService;
import com.dinoterra.dinosaur.location.FoundLocationRequest;
import com.dinoterra.dinosaur.location.FoundLocationResponse;
import com.dinoterra.dinosaur.location.FoundLocationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/dinosour")
@RequiredArgsConstructor
public class DinosourController {
    private final ImageService imageService;
    private final DinoService dinoService;
    private final FoundLocationService locationService;

    // dinos
    @PostMapping("/dinos")
    @ResponseStatus(HttpStatus.CREATED)
    public DinoResponse addDino(@RequestBody DinoRequest dinoRequest) {
        return dinoService.createDino(dinoRequest);
    }

    @PutMapping("/dinos/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public String changeDino(@PathVariable Long id, @RequestBody DinoRequest dinoRequest) {
        dinoService.changeDino(id, dinoRequest);
        return "dino successfully changed";
    }

    @GetMapping("/dinos")
    @ResponseStatus(HttpStatus.OK)
    public Page<DinoResponse> getAllDinos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String diet,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String placeLocation) {
        Pageable pageable = PageRequest.of(page, size);
        return dinoService.getAllDinos(pageable, name, type, diet, period, placeLocation);
    }

    @GetMapping("/fiveRandomDinos")
    @ResponseStatus(HttpStatus.OK)
    public List<DinoResponse> getFiveRandomDinos() {
        return dinoService.getFiveRandomDinos();
    }

    @GetMapping("/dinos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DinoResponse getDino(@PathVariable Long id) {
        return dinoService.getDino(id);
    }

    @DeleteMapping("/dinos/{id}")
    public String deleteDinoById(@PathVariable Long id) {
        dinoService.deleteDino(id);
        return "dino with id: " + id + " succesfully deleted";
    }

    // locations
    @PostMapping("/locations")
    @ResponseStatus(HttpStatus.CREATED)
    public String addLocation(@RequestBody FoundLocationRequest locationRequest) {
        locationService.createLocation(locationRequest);
        return "location successfully added";
    }

    @GetMapping("/locations")
    @ResponseStatus(HttpStatus.OK)
    public List<FoundLocationResponse> getAllLocations(@RequestParam(required = false) String place,
            @RequestParam(required = false) String period) {
        return locationService.getAllLocations(place, period);
    }

    @GetMapping("/locations/dino/{dinoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<FoundLocationResponse> getLocationsByDinoId(@PathVariable Long dinoId) {
        return locationService.getLocationsByDinoId(dinoId);
    }

    @DeleteMapping("/locations/{id}")
    public String deleteDinoLocationById(@PathVariable("id") Long id) {
        locationService.deleteLocation(id);
        return "location with id: " + id + " succesfully deleted";
    }

    // images
    @PostMapping("/images")
    @ResponseStatus(HttpStatus.CREATED)
    public String addImage(@RequestBody ImageRequest imageRequest) {
        imageService.saveImage(imageRequest);
        return "image successfully added";
    }

    @GetMapping("/images/dino/{dinoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<ImageResponse> getImagesByDinoId(@PathVariable Long dinoId) {
        return imageService.getImagesByDinoId(dinoId);
    }

    @DeleteMapping("/images/{id}")
    public String deleteDinoImageById(@PathVariable("id") Long id) {
        imageService.deleteImage(id);
        return "iamge with id: " + id + " succesfully deleted";
    }
}
