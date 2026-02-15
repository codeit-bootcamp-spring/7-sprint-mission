package com.sprint.mission.discodeit.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTClaimsSet.Builder;
import com.nimbusds.jwt.SignedJWT;
import java.util.Date;
import java.text.ParseException;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider { // 토큰을 발급, 갱신, 유효성 검사를 담당하는 컴포넌트
    private final byte[] secretKey;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtTokenProvider(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
        @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration
    ) {
        this.secretKey = secretKey.getBytes();
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    private String createToken(UUID userId, String role, long accessTokenExpiration) {
        try {
            Instant now = Instant.now();

            JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plusSeconds(accessTokenExpiration)))
                .jwtID(UUID.randomUUID().toString());

            if (role != null) {
                claimsBuilder.claim("role", role);
            }

            JWTClaimsSet claims = claimsBuilder.build();

            SignedJWT signedJWT = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims
            );

            signedJWT.sign(new MACSigner(secretKey));

            return signedJWT.serialize();

        } catch (JOSEException e) {
            throw new RuntimeException("JWT 생성 실패", e);
        }
    }


    public String createAccessToken(UUID userId, String role) {
        return createToken(userId, role, accessTokenExpiration);
    }

    public String createRefreshToken(UUID userId) {
        return createToken(userId, null, refreshTokenExpiration);
    }

    public boolean validateToken(String token) {

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            if (!signedJWT.verify(new MACVerifier(secretKey))) {
                return false;
            }

            Date expirationTime = (Date) signedJWT.getJWTClaimsSet().getExpirationTime();

            if (expirationTime == null) {
                return false; // exp 없는 토큰은 무조건 invalid
            }

            return expirationTime.after(new Date());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }

    //✅ 토큰 파싱

    private JWTClaimsSet getClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new RuntimeException("🚨JWT 파싱 실패", e);
        }
    }

    public UUID getUserId(String token) {
        return UUID.fromString(getClaims(token).getSubject());
    }

    public String getRole(String token) {
        return (String) getClaims(token).getClaim("role");
    }

    //✅ 토큰 갱신

    public String refreshAccessToken(String refreshToken, String role) {
        if (!validateToken(refreshToken)) {
            throw new IllegalArgumentException("🚨유효하지 않은 Refresh Token");
        }

        UUID userId = getUserId(refreshToken);
        return createAccessToken(userId, role);
    }
}
