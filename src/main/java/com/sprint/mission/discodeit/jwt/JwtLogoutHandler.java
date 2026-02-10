package com.sprint.mission.discodeit.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtRegistry jwtRegistry;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Cookie refreshTokenCookie = WebUtils.getCookie(request, "REFRESH_TOKEN");

        if (refreshTokenCookie != null) {
            jwtRegistry.invalidateJwtInformationByRefreshToken(refreshTokenCookie.getValue());
        }


        ResponseCookie cookie = ResponseCookie.from("REFRESH_TOKEN", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();

        //  응답 헤더에 추가하여 브라우저가 쿠키를 지우도록 함
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
