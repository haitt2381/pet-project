package com.pp.auth.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class LoginResponse {
    UUID userId;

    String token;
    String refreshToken;
    long expiresIn;
    long refreshExpiresIn;
    String tokenType;
}
