package com.dinoterra.security.user;

public record UserResponse(
        Long id,
        String name,
        String lastname,
        String username,
        String email,
        String password,
        UserRole role) {

}
