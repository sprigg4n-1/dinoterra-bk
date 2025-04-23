package com.dinoterra.security;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dinoterra.security.auth.AuthenticationResponse;
import com.dinoterra.security.auth.AuthenticationService;
import com.dinoterra.security.auth.LoginRequest;
import com.dinoterra.security.auth.RegisterRequest;
import com.dinoterra.security.config.JwtService;
import com.dinoterra.security.favorite.FavoriteDino;
import com.dinoterra.security.favorite.FavoriteDinoService;
import com.dinoterra.security.profilePhoto.ProfilePhotoRequest;
import com.dinoterra.security.profilePhoto.ProfilePhotoService;
import com.dinoterra.security.user.User;
import com.dinoterra.security.profilePhoto.UserImage;
import com.dinoterra.security.user.UserRepository;
import com.dinoterra.security.user.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("api/v1/security")
@RequiredArgsConstructor
public class SecurityController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final FavoriteDinoService favoriteDinoService;
    private final ProfilePhotoService photoService;

    @PostMapping("/users-register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/users-login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var res = authenticationService.login(request);

        ResponseCookie cookie = ResponseCookie.from("jwt", res.token())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Login successful");
    }

    @PostMapping("/users-logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged out successfully");
    }

    @GetMapping("/auth-status")
    public ResponseEntity<?> checkAuthStatus(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> "jwt".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token == null || !jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok("Authenticated");
    }

    @GetMapping("/user-data")
    public ResponseEntity<?> getUserData(HttpServletRequest request) {
        String token = Arrays.stream(request.getCookies())
                .filter(c -> "jwt".equals(c.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (token == null || !jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).body("Unauthorized: Invalid or missing token.");
        }

        try {
            User user = jwtService.extractUserFromToken(token);

            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getLastname(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole());

            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve user data.");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> userResponses = users.stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getLastname(), user.getUsername(),
                        user.getEmail(), user.getPassword(), user.getRole()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            UserResponse userResponse = new UserResponse(
                    user.get().getId(),
                    user.get().getName(),
                    user.get().getLastname(),
                    user.get().getUsername(),
                    user.get().getEmail(),
                    user.get().getPassword(),
                    user.get().getRole());

            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/users/update-profile-photo/{id}")
    public ResponseEntity<String> updateProfilePhoto(@PathVariable Long id, @RequestBody ProfilePhotoRequest request) {
        photoService.updateUserProfileImage(id, request);
        return ResponseEntity.ok("Photo added to profile");
    }

    @GetMapping("/users/profile-photo/{id}")
    public ResponseEntity<UserImage> getProfilePhoto(@PathVariable Long id) {
        UserImage userImage = photoService.getUserImageByUserId(id);
        if (userImage == null || userImage.getImage() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userImage);
    }

    @DeleteMapping("/users/profile-photo/{id}")
    public ResponseEntity<Void> deleteProfilePhoto(@PathVariable Long id) {
        boolean deleted = photoService.deleteUserImageByUserId(id);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    // fav dinos
    @PostMapping("/fav-add")
    public ResponseEntity<String> addFavorite(@RequestParam Long userId, @RequestParam Long dinosaurId) {
        favoriteDinoService.addFavorite(userId, dinosaurId);
        return ResponseEntity.ok("Dinosaur added to favorites");
    }

    @DeleteMapping("/fav-remove")
    public ResponseEntity<String> removeFavorite(@RequestParam Long userId, @RequestParam Long dinosaurId) {
        favoriteDinoService.removeFavorite(userId, dinosaurId);
        return ResponseEntity.ok("Dinosaur removed from favorites");
    }

    @GetMapping("/fav-list")
    public ResponseEntity<List<Long>> getFavorites(@RequestParam Long userId) {
        List<FavoriteDino> favorites = favoriteDinoService.getFavorites(userId);
        List<Long> dinosaurIds = favorites.stream()
                .map(FavoriteDino::getDinoId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dinosaurIds);
    }

}
