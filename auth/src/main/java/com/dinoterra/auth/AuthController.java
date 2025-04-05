package com.dinoterra.auth;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dinoterra.auth.favorite.FavoriteService;
import com.dinoterra.auth.user.LoginRequest;
import com.dinoterra.auth.user.User;
import com.dinoterra.auth.user.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/users-register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    @PostMapping("/users-login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest, HttpSession session) {
        try {
            boolean isAuthenticated = userService.authenticate(loginRequest.username(), loginRequest.password());

            if (isAuthenticated) {
                session.setAttribute("user", loginRequest.username());
                return ResponseEntity.ok("Login was successful!");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to login");
        }
    }

    @PostMapping("/users-logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logoutUser(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok("Logout was successful!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to logout");
        }
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "user with id: " + id + " succesfully deleted";
    }

    @PostMapping("/favorite")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addFavorite(@RequestParam Long userId, @RequestParam Long dinoId) {
        favoriteService.addFavorite(userId, dinoId);
        return ResponseEntity.ok("Dino added to favorites!");
    }

    @DeleteMapping("/delete-favorite/{id}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long id) {
        favoriteService.removeFavorite(id);
        return ResponseEntity.ok("Dino deleted from favorites!");
    }

}
