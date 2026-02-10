package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.exception.token.TokenException;
import com.sprint.mission.discodeit.exception.token.TokenExpiredException;
import com.sprint.mission.discodeit.exception.token.TokenInvalidException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private SecretKey key;

    @PostConstruct
    private void init() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public SecretKey getSecretKey() {
        return this.key;
    }

    public String createAccessToken(UserDto userdto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .issuer(jwtProperties.getIssuer())
                .subject(userdto.username())
                .issuedAt(now)
                .expiration(expiryDate)

                .claim("userId", userdto.id())
                .claim("username", userdto.username())
                .claim("email", userdto.email())
                .claim("roles", userdto.role())
                .claim("tokenType", "access")
                .signWith(getSecretKey())
                .compact();
    }

    public String createRefreshToken(UserDto userDto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .issuer(jwtProperties.getIssuer())
                .subject(userDto.username())
                .issuedAt(now)
                .expiration(expiryDate)

                .claim("userId", userDto.id())
                .claim("tokenType", "refresh")
                .signWith(getSecretKey())
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
            throw new TokenExpiredException(e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
            throw new TokenInvalidException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 서명: {}", e.getMessage());
            throw new TokenInvalidException(e.getMessage());
        } catch (SecurityException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
            throw new TokenInvalidException(e.getMessage());
        } catch (IllegalArgumentException e) {  // 나중에 커스텀 예외 타입으로 처리
            log.warn("JWT 토큰이 비어있는 경우: {}", e.getMessage());
            throw new TokenInvalidException(e.getMessage());
        }
    }

    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (TokenException e) {
            return false;
        }
    }
}
