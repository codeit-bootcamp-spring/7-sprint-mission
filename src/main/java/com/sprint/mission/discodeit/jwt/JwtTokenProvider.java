package com.sprint.mission.discodeit.jwt;

import com.sprint.mission.discodeit.service.dto.response.UserDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSecretKey() {
        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        // 비밀키로 HMAC-SHA256 알고리즘을 활용한 서명 생성
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Access Token 생성
    public GeneratedToken generateAccessToken(UserDto user) {

        // Jwts.builder가 Date 객체로 날짜를 세팅하기 때문에 부득이하게 Date로 생성
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpiration());

        String token = Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(jwtProperties.getIssuer()) // 발급자 (iss)
                .subject(user.getEmail()) // 주체, 사용자 식별자 (sub)
                .issuedAt(now) // 발급 시간 (iat)
                .expiration(expiryDate) // 만료 시간 (exp)
                // private claim: 우리가 인증 과정에서 필요한 사적 정보
                .claim("id", user.getId())
                .claim("email", user.getEmail())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .claim("token_type", "access")
                .signWith(getSecretKey())
                .compact();// 최종적으로 모든 정보를 압축하여 JWT 문자열을 생성.

        return new GeneratedToken(token, expiryDate.toInstant());
    }

    // Refresh Token 생성
    public GeneratedToken generateRefreshToken(UserDto user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpiration());

        String token = Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(jwtProperties.getIssuer()) // 발급자 (iss)
                .subject(user.getEmail()) // 주체, 사용자 식별자 (sub)
                .issuedAt(now) // 발급 시간 (iat)
                .expiration(expiryDate) // 만료 시간 (exp)
                // private claim: 우리가 인증 과정에서 필요한 사적 정보
                .claim("id", user.getId())
                .claim("token_type", "refresh")
                .signWith(getSecretKey())
                .compact();// 최종적으로 모든 정보를 압축하여 JWT 문자열을 생성.

        return new GeneratedToken(token, expiryDate.toInstant());
    }

    // JWT 토큰 검증 및 Claims 반환
    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey()) // 문자열로 압축된 JWT를 파싱할 때 서명을 검증
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
            throw new RuntimeException();
        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 JWT 토큰: {}", e.getMessage());
            throw new RuntimeException();
        } catch (MalformedJwtException e) {
            log.warn("잘못된 JWT 서명: {}", e.getMessage());
            throw new RuntimeException();
        } catch (SecurityException e) {
            log.warn("JWT 서명 검증 실패: {}", e.getMessage());
            throw new RuntimeException();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException();
        }
    }

    // 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    // 액세스 토큰 만료시간 조회
    public Long getAccessTokenExpirationInSeconds() {
        return jwtProperties.getAccessTokenExpiration() / 1000;
    }

}
