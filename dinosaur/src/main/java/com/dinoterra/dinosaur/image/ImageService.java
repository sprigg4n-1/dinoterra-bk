package com.dinoterra.dinosaur.image;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.dinoterra.dinosaur.dino.Dino;
import com.dinoterra.dinosaur.dino.DinoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {
    private final ImageRepository imageRepository;
    private final DinoRepository dinoRepository;

    public void saveImage(ImageRequest imageRequest) {
        DinoImage image = mapToImageEntity(imageRequest);
        imageRepository.save(image);
    }

    public List<ImageResponse> getImagesByDinoId(Long dinoId) {
        Dino dino = dinoRepository.findById(dinoId)
                .orElseThrow(() -> new RuntimeException("Dino not found with ID " + dinoId));

        List<DinoImage> images = imageRepository.findByDino(dino);

        return images.stream().map(image -> mapToImageRes(image, dino.getId())).collect(Collectors.toList());
    }

    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    private ImageResponse mapToImageRes(DinoImage dinoImage, Long dinoId) {
        return new ImageResponse(dinoImage.getId(), dinoImage.getImage(), dinoImage.getFileName(), dinoId);
    }

    private byte[] decodeBase64Image(String base64Image) {
        String base64Data = base64Image;
        String prefix = "data:image/";
        String separator = ";base64,";

        if (base64Image.startsWith(prefix)) {
            int separatorIndex = base64Image.indexOf(separator);
            if (separatorIndex > 0) {
                base64Data = base64Image.substring(separatorIndex + separator.length());
            }
        }

        try {
            return Base64.getDecoder().decode(base64Data);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error decoding Base64 image", e);
        }
    }

    private DinoImage mapToImageEntity(ImageRequest imageRequest) {
        Dino dino = dinoRepository.findById(imageRequest.dino_id())
                .orElseThrow(() -> new RuntimeException("Dino not found with ID " + imageRequest.dino_id()));

        byte[] imageBytes = decodeBase64Image(imageRequest.imagePath());

        DinoImage image = new DinoImage();
        image.setDino(dino);
        image.setFileName(imageRequest.fileName());
        image.setImage(imageBytes);

        return image;
    }
}
