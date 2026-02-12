package com.sprint.mission.discodeit.global.config.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

/**
 * 의도적으로 ACCESS 토큰과 REFRESH 토큰 메서드를 분리
 *
 */
@Component
@Slf4j
public class JwtProvider {
    // 쿠키 이름도 JWT 인증 규약의 일부
    public static final String REFRESH_COOKIE_NAME = "refresh";

    private final JwtProperties jwtProperties;

    // JWT 서명/검증을 위한 HMAC(MAC) 기반 컴포넌트
    // HMAC은 대칭키 방식이므로 Signer와 Verifier가 동일한 Secret Key를 사용
    // MACSigner  : JWT의 Signature를 생성
    // MACVerifier: JWT Signature의 무결성을 검증
    private final MACSigner signer;
    private final MACVerifier verifier;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        SecretKey secretKey = getSecretKey();
        try {
            this.signer = new MACSigner(secretKey);
            this.verifier = new MACVerifier(secretKey);
        } catch (JOSEException e) {
            log.error("JWT 컴포넌트 초기화 실패 - Secret길이 또는 알고리즘 설정 문제", e);
            throw new IllegalStateException();
        }
    }

    private SecretKey getSecretKey() {
        byte[] secretBytes = jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            log.error("secret은 최소 32자 이상이어야 합니다.");
            throw new IllegalArgumentException();
        }

        SecretKeySpec secretKey = new SecretKeySpec(secretBytes, "HmacSHA256");
        log.debug("비밀 키 초기화");
        return secretKey;
    }

    public String generateAccessToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.accessTokenExpiration());

        // PayLoad clams Set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.issuer())
                .subject(username)
                .issueTime(now)
                .expirationTime(expiryDate)
                .claim("token_type", "access")
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            log.error("토큰 서명에 실패하였습니다.");
            throw new RuntimeException(e);
        }

        String token = signedJWT.serialize();
        log.info("{}의 ACCESS 토큰 발급: {}", username, token);

        return token;
    }

    public String generateRefreshToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperties.refreshTokenExpiration());

        // PayLoad clams Set
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.issuer())
                .subject(username.toString())
                .issueTime(now)
                .expirationTime(expiryDate)
                .claim("token_type", "refresh")
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claimsSet
        );

        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            log.error("토큰 서명에 실패하였습니다.");
            throw new RuntimeException(e);
        }

        String token = signedJWT.serialize();
        log.info("{}의 Refresh 토큰 발급: {}", username, token);

        return token;
    }

    public boolean validateAccessToken(String token) {
        try {
            SignedJWT signedJWt = SignedJWT.parse(token);

            // 서명 검증
            if (!signedJWt.verify(verifier)) {
                log.debug("JWT ACCESS 토큰 서명 검증 실패");
                return false;
            }

            JWTClaimsSet claimsSet = signedJWt.getJWTClaimsSet();

            // 만료시간 검증
            Date expiration = claimsSet.getExpirationTime();
            if (expiration == null || expiration.before(new Date())) {
                log.debug("JWT ACCESS 토큰 만료");
                return false;
            }

            // ACCESS 토큰 여부 검증
            if (!"access".equals(claimsSet.getClaim("token_type"))) {
                log.debug("Access Token이 아님");
                return false;
            }

            // ISSUER 검증
            if (!jwtProperties.issuer().equals(claimsSet.getIssuer())) {
                log.debug("Issuer 불일치");
                return false;
            }

            return true;

        } catch (Exception e) {
            log.debug("JWT 파싱/검증 실패");
            throw new RuntimeException(e);
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            SignedJWT signedJWt = SignedJWT.parse(token);

            // 서명 검증
            if (!signedJWt.verify(verifier)) {
                log.debug("JWT Refresh 토큰 서명 검증 실패");
                return false;
            }

            JWTClaimsSet claimsSet = signedJWt.getJWTClaimsSet();

            // 만료시간 검증
            Date expiration = claimsSet.getExpirationTime();
            if (expiration == null || expiration.before(new Date())) {
                log.debug("JWT Refresh 토큰 만료");
                return false;
            }

            // Refresh 토큰 여부 검증
            if (!"refresh".equals(claimsSet.getClaim("token_type"))) {
                log.debug("Refresh Token이 아님");
                return false;
            }

            // ISSUER 검증
            if (!jwtProperties.issuer().equals(claimsSet.getIssuer())) {
                log.debug("issuer 불일치");
                return false;
            }

            return true;

        } catch (Exception e) {
            log.debug("JWT 파싱/검증 실패");
            throw new RuntimeException(e);
        }
    }

    public long getRefreshTokenExpirationMills() {
        return jwtProperties.refreshTokenExpiration();
    }

    public String extractUsername(String accessToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            return signedJWT.getJWTClaimsSet().getSubject();

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
