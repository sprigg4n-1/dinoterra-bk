package com.dinoterra.security.profilePhoto;

import java.util.Base64;

import org.springframework.stereotype.Service;

import com.dinoterra.security.user.User;
import com.dinoterra.security.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfilePhotoService {
    private final ProfilePhotoRepository photoRepository;
    private final UserRepository userRepository;

    public void updateUserProfileImage(Long userId, ProfilePhotoRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        byte[] imageData = decodeBase64Image(request.imagePath());

        UserImage image = user.getProfileImage();
        if (image == null) {
            image = new UserImage();
            image.setUser(user);
        }

        image.setImage(imageData);

        user.setProfileImage(image);
        userRepository.save(user);
    }

    public UserImage getUserImageByUserId(Long userId) {
        return photoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User image not found"));
    }

    public boolean deleteUserImageByUserId(Long userId) {
        UserImage userImage = photoRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User image not found"));
        ;
        if (userImage != null) {
            photoRepository.delete(userImage);
            return true;
        }
        return false;
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
}
