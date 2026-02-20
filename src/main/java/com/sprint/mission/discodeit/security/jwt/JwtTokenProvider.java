package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.jwt.BusinessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
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

    private SecretKey getSecretKey(boolean isAccessToken) {
        byte[] keyBytes;

        if (isAccessToken) {
            keyBytes = jwtProperties.getAccessSecret().getBytes(StandardCharsets.UTF_8);
        } else {
            keyBytes = jwtProperties.getRefreshSecret().getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)

                .claim("userId", user.getId())
                .claim("email", user.getEmail())
                .claim("role", user.getRole())
                .claim("token_type", "access")
                .signWith(getSecretKey(true))
                .compact();
    }

    public String generateRefreshToken(User user) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        return Jwts.builder()
                .header()
                    .type("JWT")
                    .and()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getUsername())
                .issuedAt(now)
                .expiration(expiryDate)

                .claim("userId", user.getId())
                .claim("token_type", "refresh")
                .signWith(getSecretKey(false))
                .compact();
    }

    // refresh 토큰 검증
    public Claims validateToken(String token, boolean isAccessToken) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey(isAccessToken)) // 문자열로 압축된 JWT를 파싱할 때 서명을 검
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
            throw new BusinessException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 서명: {}", e.getMessage());
            throw new BusinessException(ErrorCode.MALFORMED_TOKEN);
        } catch (SecurityException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_SIGNATURE);
        } catch (IllegalArgumentException e) { // 나중에 커스텀 예외 타입으로 처리하세요.
            log.warn("JWT 토큰이 비어있는 경우: {}", e.getMessage());
            throw new BusinessException(ErrorCode.EMPTY_OR_INVALID_TOKEN);
        }
    }

    // 토큰 유효성 검사
    public boolean isRefreshTokenValid(String token) {
        try {
            validateToken(token, false);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    public boolean isAccessTokenValid(String token) {
        try {
            validateToken(token, true);
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    // 액세스 토큰 만료시간 조회
    public Long getAccessTokenExpirationInSeconds() {
        return jwtProperties.getAccessTokenExpiration() / 1000;
    }

}
