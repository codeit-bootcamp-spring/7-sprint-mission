package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;

        for (Cookie cookie : cookies) {
            if ("REFRESH_TOKEN".equals(cookie.getName())) {

                String refreshToken = cookie.getValue();

                refreshTokenService.deleteRefreshToken(refreshToken);

                Cookie deleteCookie = new Cookie("REFRESH_TOKEN", null);
                deleteCookie.setHttpOnly(true);
                deleteCookie.setPath("/");
                deleteCookie.setSecure(false); // 로컬 기준
                deleteCookie.setMaxAge(0);

                response.addCookie(deleteCookie);
            }
        }
    }
}
