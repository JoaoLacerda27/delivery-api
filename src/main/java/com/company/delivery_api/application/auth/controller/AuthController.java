package com.company.delivery_api.application.auth.controller;

import com.company.delivery_api.application.auth.dto.AuthResponse;
import com.company.delivery_api.application.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "OAuth2 authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @Value("${app.frontend.url:http://localhost:5173}")
    private String frontendUrl;

    @GetMapping("/login")
    @Operation(summary = "Get login URL", description = "Returns the Google OAuth2 login URL")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Please visit: /oauth2/authorization/google");
    }

    @GetMapping("/login-success")
    @Operation(summary = "OAuth2 login success callback", description = "Handles successful OAuth2 login and redirects to frontend with token in URL")
    public void loginSuccess(Authentication authentication, HttpServletResponse response) throws IOException {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            AuthResponse authResponse = authService.createAuthResponse(oauth2User);
            
            String redirectUrl = String.format("%s/auth/callback?token=%s&email=%s&name=%s&picture=%s",
                    frontendUrl,
                    URLEncoder.encode(authResponse.token(), StandardCharsets.UTF_8),
                    URLEncoder.encode(authResponse.email() != null ? authResponse.email() : "", StandardCharsets.UTF_8),
                    URLEncoder.encode(authResponse.name() != null ? authResponse.name() : "", StandardCharsets.UTF_8),
                    URLEncoder.encode(authResponse.picture() != null ? authResponse.picture() : "", StandardCharsets.UTF_8)
            );
            
            response.sendRedirect(redirectUrl);
        } else {
            String errorUrl = String.format("%s/auth/callback?error=authentication_failed",
                    frontendUrl);
            response.sendRedirect(errorUrl);
        }
    }

    @GetMapping("/login-failure")
    @Operation(summary = "OAuth2 login failure callback", description = "Handles failed OAuth2 login and redirects to frontend with error")
    public void loginFailure(HttpServletResponse response) throws IOException {
        String errorUrl = String.format("%s/auth/callback?error=login_failed",
                frontendUrl);
        response.sendRedirect(errorUrl);
    }

    @GetMapping("/user")
    @Operation(summary = "Get current user", description = "Returns information about the currently authenticated user")
    public ResponseEntity<AuthResponse> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User oauth2User) {
            AuthResponse response = authService.createAuthResponse(oauth2User);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Logs out the current user")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        return ResponseEntity.ok("Logged out successfully");
    }
}

