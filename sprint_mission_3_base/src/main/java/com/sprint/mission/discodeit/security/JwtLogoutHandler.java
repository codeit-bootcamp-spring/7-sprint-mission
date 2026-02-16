package com.sprint.mission.discodeit.security;

import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String refreshToken = extractRefreshToken(request);
        if (refreshToken != null && jwtTokenProvider.validate(refreshToken)) {
            JWTClaimsSet claims = jwtTokenProvider.parseClaims(refreshToken);
            Object typ = claims.getClaim(JwtTokenProvider.CLAIM_TOKEN_TYPE);
            if (JwtTokenProvider.TYPE_REFRESH.equals(typ)) {
                String userIdStr = String.valueOf(claims.getClaim(JwtTokenProvider.CLAIM_USER_ID));
                if (userIdStr != null) {
                    refreshTokenService.delete(UUID.fromString(userIdStr));
                }
            }
        }

        Cookie cookie = new Cookie("REFRESH_TOKEN", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        return Arrays.stream(cookies)
                .filter(c -> "REFRESH_TOKEN".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
