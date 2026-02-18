package com.sprint.mission.discodeit.common.security.jwt;

import com.sprint.mission.discodeit.common.exception.auth.RoleNotFoundException;
import com.sprint.mission.discodeit.common.exception.auth.TokenNotFoundException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.response.auth.AuthTokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-seconds}")
    private long accessTokenSeconds;

    @Value("${jwt.refresh-token-seconds}")
    private long refreshTokenSeconds;

    private SecretKey key;

    @PostConstruct
    void init() {
        byte[] bytes;

        try {
            bytes = Base64.getDecoder().decode(secret);
        } catch (Exception e) {
            bytes = secret.getBytes();
        }

        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateAccessToken(UUID userId, String role) {
        return generateToken(userId, role, TokenType.ACCESS, accessTokenSeconds);
    }

    public String generateRefreshToken(UUID userId, String role) {
        return generateToken(userId, role, TokenType.REFRESH, refreshTokenSeconds);
    }

    public String generateToken(UUID userId, String role, TokenType tokenType, long seconds) {
        if (userId == null) {
            throw new UserNotFoundException("User not found");
        }
        if (role == null || role.isEmpty()) {
            throw new RoleNotFoundException("Role not found");
        }

        Instant now = Instant.now();
        Instant exp = now.plusSeconds(seconds);

        return Jwts.builder()
                .issuer(issuer)
                .subject(userId.toString())
                .claims(Map.of(
                        "role", role,
                        "tokenType", tokenType.name()
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key,Jwts.SIG.HS256)
                .compact();
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isExpired(String token) {
        try {
            Claims claims = parseClaims(token);
            Date expiration = claims.getExpiration();
            return expiration == null || expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public boolean isTokenType(String token, TokenType tokenType) {
        Claims claims = parseClaims(token);
        String type = claims.get("tokenType", String.class);
        return tokenType.name().equals(type);
    }

    public String reAccessToken(String refreshToken) {
        if (!validate(refreshToken)) {
            throw new JwtException("Invalid refresh token");
        }
        if (!isTokenType(refreshToken, TokenType.REFRESH)) {
            throw new JwtException("tokenType is not REFRESH");
        }

        Claims claims = parseClaims(refreshToken);
        UUID userId = UUID.fromString(claims.getSubject());
        String role = claims.get("role", String.class);

        return generateAccessToken(userId, role);
    }


    public Claims parseClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new TokenNotFoundException("Token not found");
        }

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String resolveBearer(String authorizationHeaderValue) {
        if(authorizationHeaderValue == null) {
            return null;
        }
        String v = authorizationHeaderValue.trim();

        if(v.regionMatches(true, 0, "Bearer ", 0, 7)) {
            return v.substring(7).trim();
        }

        return null;
    }

    public AuthTokenResponseDto issueTokenPair(UUID userId, String role) {
        return new AuthTokenResponseDto(
                generateAccessToken(userId, role),
                generateRefreshToken(userId, role)
        );
    }

    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean validateAccessToken(String token) {
        return validate(token) && !isExpired(token) && isTokenType(token, TokenType.ACCESS);
    }

    public boolean validateRefreshToken(String token) {
        return validate(token) && !isExpired(token) && isTokenType(token, TokenType.REFRESH);
    }

    public Instant getExpirationInstant(String token) {
        Date exp = parseClaims(token).getExpiration();
        return exp == null ? null : exp.toInstant();
    }
}
