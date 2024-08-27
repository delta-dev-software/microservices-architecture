package com.example.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
public class JwtTokenService {

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.secret}")
    private String secret;

    public JwtTokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(3600); // 1 hour

        // Create JwtClaimsSet with claims, expiration, and issued time
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .build();

        // Create JwtEncoderParameters from the JwtClaimsSet
        JwtEncoderParameters parameters = JwtEncoderParameters.from(claimsSet);

        // Encode the JWT
        return jwtEncoder.encode(parameters).getTokenValue();
    }
}

