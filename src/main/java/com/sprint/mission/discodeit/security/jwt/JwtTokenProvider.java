package com.sprint.mission.discodeit.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.security.jwt.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    private SecretKey getSecretKey() {
        byte[] keyBytes = jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // Access Token 생성
    public String generateAccessToken(User user) throws JOSEException {

        // Jwts.builder가 Date 객체로 날짜를 세팅하기 때문에 부득이하게 Date로 생성
        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtProperties.getAccessTokenExpirationMs());

        // 페이로드 claims
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.getIssuer()) // 토큰 발급자 (iss)
                .subject(user.getEmail()) // 주체, 사용할 주체, 사용자 식별자 (sub)
                .issueTime(now) // 발급 시간  (iat)
                .expirationTime(expiryDate) // 만료시간 (exp)
                // private claim: 우리가 인증 과정에서 필요한 사적 정보
                .claim("user_id", user.getId()) // 사용자 인증정보
                .claim("name", user.getUsername())
                .claim("role", user.getRole())
                .claim("email", user.getEmail())
                .claim("token_type", "access")
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        SignedJWT signedJWT = new SignedJWT(header, claims);

        signedJWT.sign(new MACSigner(getSecretKey()));

        return signedJWT.serialize();
    }

    // Refresh Token 생성
    public String generateRefreshToken(User user) throws JOSEException {

        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtProperties.getRefreshTokenExpirationMs());

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.getIssuer())
                .subject(user.getEmail())
                .issueTime(now)
                .expirationTime(expiryDate)
                .claim("user_id", user.getId())
                .claim("token_type", "refresh")
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        SignedJWT signedJWT = new SignedJWT(header, claims);

        signedJWT.sign(new MACSigner(getSecretKey()));

        return signedJWT.serialize();
    }

    // JWT 토큰 검증 및 Claims 반환
    public JWTClaimsSet validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            MACVerifier verifier = new MACVerifier(getSecretKey());

            if (!signedJWT.verify(verifier)) {
                throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
            }

            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            Date expirationTime = claims.getExpirationTime();

            if (expirationTime == null || expirationTime.before(new Date())) {
                throw new DiscodeitException(ErrorCode.EXPIRED_TOKEN);
            }

            return claims;

        } catch (ParseException e) {
            throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
        } catch (JOSEException e) {
            throw new DiscodeitException(ErrorCode.INVALID_TOKEN);
        }
    }

    // 토큰 유효성 검사
    public boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (DiscodeitException e) {
            return false;
        }
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserId(String token) throws ParseException {
        JWTClaimsSet claims = validateToken(token);
        return claims.getLongClaim("user_id");
    }
}
