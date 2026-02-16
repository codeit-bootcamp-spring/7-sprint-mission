package com.sprint.mission.discodeit.controller;

import com.nimbusds.jwt.JWTClaimsSet;
import com.sprint.mission.discodeit.dto.JwtDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.security.RefreshToken;
import com.sprint.mission.discodeit.security.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;

    @GetMapping("/csrf-token")
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshToken(request).orElse(null);
        if (refreshToken == null || !jwtTokenProvider.validate(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        JWTClaimsSet claims = jwtTokenProvider.parseClaims(refreshToken);
        Object typ = claims.getClaim(JwtTokenProvider.CLAIM_TOKEN_TYPE);
        if (!JwtTokenProvider.TYPE_REFRESH.equals(typ)) {
            return ResponseEntity.status(401).build();
        }

        String userIdStr = String.valueOf(claims.getClaim(JwtTokenProvider.CLAIM_USER_ID));
        String tokenId = String.valueOf(claims.getClaim(JwtTokenProvider.CLAIM_JTI));
        if (userIdStr == null || tokenId == null) {
            return ResponseEntity.status(401).build();
        }

        UUID userId = UUID.fromString(userIdStr);
        RefreshToken saved = refreshTokenService.find(userId).orElse(null);
        if (saved == null) {
            return ResponseEntity.status(401).build();
        }

        if (!saved.getTokenId().equals(tokenId)) {
            return ResponseEntity.status(401).build();
        }

        if (saved.getExpiresAt().isBefore(Instant.now())) {
            return ResponseEntity.status(401).build();
        }

        String username = claims.getSubject();
        DiscodeitUserDetails principal = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(username);

        String accessToken = jwtTokenProvider.createAccessToken(principal);
        JwtTokenProvider.RefreshTokenResult rotated = jwtTokenProvider.createRefreshToken(principal);

        refreshTokenService.upsert(userId, rotated.tokenId(), rotated.expiresAt());

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", rotated.token());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) jwtTokenProvider.getRefreshTokenMaxAgeSeconds());
        response.addCookie(refreshCookie);

        return ResponseEntity.ok(new JwtDto(principal.getUserDto(), accessToken));
    }

    private Optional<String> extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        return Arrays.stream(cookies)
                .filter(c -> "REFRESH_TOKEN".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
