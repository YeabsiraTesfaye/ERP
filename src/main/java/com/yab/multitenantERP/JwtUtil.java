package com.yab.multitenantERP;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // Generate JWT Token
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())  // Use Jwts.SIG for signing
                .compact();
    }

    // Extract Email from Token
    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Method to validate the token
    public boolean validateToken(String token) {
        try {
            Jwts.builder()
                    .signWith(getSigningKey());
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Invalid token
        }
    }

    public Claims validate_token(String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(secret.getBytes());
        // Parses and validates the token. Throws an exception if invalid.
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
