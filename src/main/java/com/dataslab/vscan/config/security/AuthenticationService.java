package com.dataslab.vscan.config.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthenticationService {

    public static final String AUTH_TOKEN_HEADER_NAME = "X-API-KEY";
    private static final List<GrantedAuthority> SIMPLE_AUTHORITIES = List.of(new SimpleGrantedAuthority("API_ROLE"));

    private final String key;

    public AuthenticationService(@NonNull @Value("${vscan-api.api-key}") String key) {
        this.key = key;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        log.trace("Performing authentication for path: {}", request.getRequestURI());

        String apiKey = request.getHeader(AUTH_TOKEN_HEADER_NAME);
        if (apiKey == null || !apiKey.equals(key)) {
            log.warn("No api-key or invalid api-key provided: {}. Remote address: {}", apiKey, request.getRemoteAddr());
            throw new BadCredentialsException("Invalid API Key");
        }

        return new ApiKeyAuthentication(apiKey, SIMPLE_AUTHORITIES);
    }
}
