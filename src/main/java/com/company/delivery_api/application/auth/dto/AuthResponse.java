package com.company.delivery_api.application.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response with user information and token")
public record AuthResponse(
        @Schema(example = "user@example.com", description = "User email")
        String email,
        @Schema(example = "John Doe", description = "User name")
        String name,
        @Schema(example = "https://lh3.googleusercontent.com/...", description = "User profile picture URL")
        String picture,
        @Schema(example = "eyJhbGciOiJSUzI1NiIs...", description = "JWT token for API authentication")
        String token
) {}


