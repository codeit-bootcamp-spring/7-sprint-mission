package com.sprint.mission.discodeit.security.service;

import com.sprint.mission.discodeit.config.JwtProperties;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.domain.auth.InSufficientAccessException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSecretKey() {

        byte[] keyBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(User user){

        Instant now = Instant.now();
        Instant expirationDate = now.plusSeconds(jwtProperties.getAccessTokenExpiration());
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationDate))
                .claim("user_id",user.getId())
                .claim("name",user.getUserName())
                .claim("email",user.getEmail())
                .claim("role",user.getRole())
                .claim("token_type","access")
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(User user){

        Instant now = Instant.now();
        Instant expirationDate = now.plusSeconds(jwtProperties.getRefreshTokenExpiration());
        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(user.getUserName())
                .subject(user.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationDate))
                .claim("user_id",user.getId())
                .claim("token_type","refresh")
                .signWith(getSecretKey())
                .compact();
    }

    public String updateRefreshToken(User user,Date expirationDate){

        return Jwts.builder()
                .header()
                .type("JWT")
                .and()
                .issuer(user.getUserName())
                .subject(user.getEmail())
                .issuedAt(Date.from(Instant.now()))
                .expiration(expirationDate)
                .claim("user_id",user.getId())
                .claim("token_type","refresh")
                .signWith(getSecretKey())
                .compact();
    }

    public long getAccessTokenExpirationTime(){
        return jwtProperties.getAccessTokenExpiration()/1000;
    }

    public Claims validateToken(String token){
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey()) // 문자열로 압축된 jwt를 파싱할 때 서명을 검증
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT token: {} ", e.getMessage());

        } catch (UnsupportedJwtException e) {
            log.warn("지원되지 않는 jwt token: {} ", e.getMessage());
        }
        catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {} ", e.getMessage());
        }
        catch (SecurityException e) {
            log.warn("Jwt sign verification failed: {} ", e.getMessage());
        }
        catch (IllegalArgumentException e) {
            throw new InSufficientAccessException("test test");
        }
        return null;
    }

    public boolean isTokenValid(String token){

        try {
            validateToken(token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public UUID getUserIdFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        String userId = claims.get("user_id", String.class);
        return UUID.fromString(userId);
    }

    public Date getRefreshTokenExpirationDate(String refreshToken){
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
        return claims.getExpiration();
    }

}
