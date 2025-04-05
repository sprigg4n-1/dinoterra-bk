package com.dinoterra.dinosaur.image;

public record ImageResponse(Long id, byte[] image, String fileName, Long dino_id) {
    
}
