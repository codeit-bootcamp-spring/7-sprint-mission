package com.sprint.mission.discodeit.security.jwt;

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

    private SecretKey accessKey;
    private SecretKey refreshKey;

    @PostConstruct
    private void init() {
        byte[] accessKeyBytes = jwtProperties.getAccessSecret().getBytes(StandardCharsets.UTF_8);
        this.accessKey = Keys.hmacShaKeyFor(accessKeyBytes);
        byte[] refreshKeyBytes = jwtProperties.getRefreshSecret().getBytes(StandardCharsets.UTF_8);
        this.refreshKey = Keys.hmacShaKeyFor(refreshKeyBytes);
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
                .claim("roles", userdto.role())
                .claim("tokenType", "access")
                .signWith(accessKey)
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
                .signWith(refreshKey)
                .compact();
    }

    private Claims parseToken(String token, SecretKey key) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
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

    public Claims validateAccessToken(String token) {
        return parseToken(token, accessKey);
    }

    public Claims validateRefreshToken(String token) {
        return parseToken(token, refreshKey);
    }

    public boolean isAccessTokenValid(String token) {
        try {
            validateAccessToken(token);
            return true;
        } catch (TokenException e) {
            return false;
        }
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            validateRefreshToken(token);
            return true;
        } catch (TokenException e) {
            return false;
        }
    }

    public String getUsernameFromAccessToken(String token) {
        return validateAccessToken(token).getSubject();
    }
    public String getUsernameFromRefreshToken(String token) {
        return validateRefreshToken(token).getSubject();
    }
}