package ch.wiss.m223.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${myapp.jwtSecret}")
    private String jwtSecret;

    @Value("${myapp.jwtExpirationMs}")
    private int jwtExpirationMs;

    // =========================
    // TOKEN GENERATION
    // =========================
    public String generateToken(String username) {

        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSignKey())
                .compact();
    }

    // =========================
    // SIGNING KEY
    // =========================
    private Key getSignKey() {
        // FIX: kein BASE64 decode, weil dein secret NICHT base64 ist
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // =========================
    // EXTRACT USERNAME
    // =========================
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // =========================
    // EXTRACT EXPIRATION
    // =========================
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // =========================
    // GENERIC CLAIM EXTRACTOR
    // =========================
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    // =========================
    // PARSE TOKEN
    // =========================
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // =========================
    // CHECK EXPIRATION
    // =========================
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // =========================
    // VALIDATION (SAFE)
    // =========================
    public Boolean validateToken(String token) {
        try {
            extractAllClaims(token); // checks signature + structure
            return !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }
}