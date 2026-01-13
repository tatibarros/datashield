package com.datashield.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import jakarta.annotation.PostConstruct;

@Component
@Slf4j
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        // Initialize and cache the signing key once at startup to ensure token validity
        try {
            if (jwtSecret != null && jwtSecret.length() >= 64) {
                byte[] keyBytes = jwtSecret.getBytes();
                this.signingKey = Keys.hmacShaKeyFor(keyBytes);
                log.info("Using provided JWT secret for HS512 with {} bits", keyBytes.length * 8);
                return;
            }

            // Try base64 decode if possible
            try {
                byte[] decoded = Base64.getDecoder().decode(jwtSecret);
                if (decoded.length >= 64) {
                    this.signingKey = Keys.hmacShaKeyFor(decoded);
                    log.info("Using provided base64 JWT secret for HS512 with {} bits", decoded.length * 8);
                    return;
                }
            } catch (IllegalArgumentException e) {
                // ignore, will fallback to generating a key
            }

            log.warn("JWT secret not provided or too short; generating secure 512-bit key for HS512. Note: tokens will not be shareable across restarts.");
            this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } catch (Exception e) {
            log.error("Failed to initialize JWT signing key, generating fallback key", e);
            this.signingKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(this.signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(this.signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.signingKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }
}
