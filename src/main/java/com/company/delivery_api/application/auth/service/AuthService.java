package com.company.delivery_api.application.auth.service;

import com.company.delivery_api.application.auth.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtEncoder jwtEncoder;

    public AuthResponse createAuthResponse(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        
        String token = generateJwtToken(oauth2User);
        
        return new AuthResponse(
                email != null ? email : "",
                name != null ? name : "",
                picture != null ? picture : "",
                token
        );
    }

    private String generateJwtToken(OAuth2User oauth2User) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600);
        
        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        
        if (email == null || email.isEmpty()) {
            email = "unknown";
        }
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("delivery-api")
                .issuedAt(now)
                .expiresAt(expiry)
                .notBefore(now)
                .subject(email)
                .claim("email", email)
                .claim("name", name != null ? name : "")
                .claim("picture", picture != null ? picture : "")
                .build();
        
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

