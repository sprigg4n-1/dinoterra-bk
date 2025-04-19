package com.dinoterra.security.auth;

import com.dinoterra.security.user.UserRole;

public record RegisterRequest(String name, String lastname, String username, String password, String email,
        UserRole role) {
}
