package com.company.delivery_api.shared.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Custom JWT Authentication Converter
 * 
 * Extracts authorities from JWT claims (roles, scopes, etc.)
 * 
 * Example JWT claims:
 * {
 *   "scope": "read write",
 *   "roles": ["USER", "ADMIN"],
 *   "authorities": ["SCOPE_read", "SCOPE_write"]
 * }
 */
public class JwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public JwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                defaultConverter.convert(jwt).stream(),
                extractCustomAuthorities(jwt).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(jwt, authorities);
    }

    /**
     * Extract custom authorities from JWT claims
     * Supports:
     * - "scope" claim (space-separated): "read write" -> SCOPE_read, SCOPE_write
     * - "roles" claim (array): ["USER", "ADMIN"] -> ROLE_USER, ROLE_ADMIN
     * - "authorities" claim (array): ["SCOPE_read"] -> SCOPE_read
     */
    private Collection<GrantedAuthority> extractCustomAuthorities(Jwt jwt) {
        return Stream.of(
                extractScopes(jwt),
                extractRoles(jwt),
                extractAuthorities(jwt)
        ).flatMap(Collection::stream)
         .collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractScopes(Jwt jwt) {
        String scope = jwt.getClaimAsString("scope");
        if (scope == null || scope.isBlank()) {
            return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
        }

        return Stream.of(scope.split(" "))
                .map(s -> "SCOPE_" + s.trim())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractRoles(Jwt jwt) {
        Object rolesClaim = jwt.getClaim("roles");
        if (rolesClaim == null) {
            return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
        }

        if (rolesClaim instanceof Collection<?> roles) {
            return roles.stream()
                    .map(role -> "ROLE_" + role.toString())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
    }

    private Collection<GrantedAuthority> extractAuthorities(Jwt jwt) {
        Object authoritiesClaim = jwt.getClaim("authorities");
        if (authoritiesClaim == null) {
            return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
        }

        if (authoritiesClaim instanceof Collection<?> authorities) {
            return authorities.stream()
                    .map(auth -> auth.toString())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }

        return Stream.<GrantedAuthority>empty().collect(Collectors.toSet());
    }
}

