package com.sprint.mission.discodeit.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    public static final String CLAIM_USER_ID = "uid";
    public static final String CLAIM_TOKEN_TYPE = "typ";
    public static final String CLAIM_ROLES = "roles";
    public static final String CLAIM_JTI = "jti";

    public static final String TYPE_ACCESS = "access";
    public static final String TYPE_REFRESH = "refresh";

    private final JwtProperties jwtProperties;

    public String createAccessToken(DiscodeitUserDetails principal) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(jwtProperties.accessTtlSeconds());

        String role = principal.getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority())
                .orElse("ROLE_USER");

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.issuer())
                .subject(principal.getUsername())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(exp))
                .claim(CLAIM_USER_ID, principal.getUser().getId().toString())
                .claim(CLAIM_TOKEN_TYPE, TYPE_ACCESS)
                .claim(CLAIM_ROLES, role)
                .build();

        return sign(claims);
    }

    public RefreshTokenResult createRefreshToken(DiscodeitUserDetails principal) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(jwtProperties.refreshTtlSeconds());
        String jti = UUID.randomUUID().toString();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .issuer(jwtProperties.issuer())
                .subject(principal.getUsername())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(exp))
                .claim(CLAIM_USER_ID, principal.getUser().getId().toString())
                .claim(CLAIM_TOKEN_TYPE, TYPE_REFRESH)
                .claim(CLAIM_JTI, jti)
                .build();

        return new RefreshTokenResult(sign(claims), jti, exp);
    }

    public boolean validate(String token) {
        try {
            SignedJWT jwt = SignedJWT.parse(token);
            if (!jwt.verify(new MACVerifier(secretBytes()))) return false;
            Date exp = jwt.getJWTClaimsSet().getExpirationTime();
            return exp != null && exp.after(new Date());
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }

    public JWTClaimsSet parseClaims(String token) {
        try {
            return SignedJWT.parse(token).getJWTClaimsSet();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid token");
        }
    }

    public long getRefreshTokenMaxAgeSeconds() {
        return jwtProperties.refreshTtlSeconds();
    }

    private String sign(JWTClaimsSet claims) {
        try {
            SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
            jwt.sign(new MACSigner(secretBytes()));
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new IllegalStateException("Token signing failed");
        }
    }

    private byte[] secretBytes() {
        return jwtProperties.secret().getBytes(StandardCharsets.UTF_8);
    }

    public record RefreshTokenResult(String token, String tokenId, Instant expiresAt) {
    }
}
