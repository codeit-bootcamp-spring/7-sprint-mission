package com.sprint.mission.discodeit.common.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Getter
@Component
public class JwtProvider {
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
    private final String issuer;
    private final SecretKey signingKey;
    public static final String REFRESH_TOKEN_NAME = "REFRESH_TOKEN";

    public JwtProvider(JwtProperties properties) {
        this.signingKey = Keys.hmacShaKeyFor(properties.getKey().getBytes());
        this.accessTokenExpiration = properties.getAccessTokenExpiration();
        this.refreshTokenExpiration = properties.getRefreshTokenExpiration();
        this.issuer = properties.getIssuer();
    }

    private String createToken(UUID userid, String username, String role, long expiration) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userid.toString())
                .claim("username", username)
                .claim("role", role)
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expiration)))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String createAccessToken(UUID userId, String username, String role) {
        return createToken(userId, username, role, accessTokenExpiration);
    }

    public String createRefreshToken(UUID userId, String username, String role) {
        return createToken(userId, username, role, refreshTokenExpiration);
    }

    public String getUsername(String token) {
        return parseClaims(token).get("username", String.class);
    }

    public String expireToken(String token) {
        return Jwts.builder()
                .setClaims(parseClaims(token))
                .setExpiration(Date.from(Instant.now()))
                .signWith(signingKey)
                .compact();
    }

    public UUID getUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }
}
