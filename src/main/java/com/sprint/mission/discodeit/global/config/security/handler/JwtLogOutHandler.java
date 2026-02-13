package com.sprint.mission.discodeit.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtLogOutHandler implements LogoutHandler {
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Cookie refreshCookie = new Cookie(JwtProvider.REFRESH_COOKIE_NAME, null);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(false);
        refreshCookie.setMaxAge(0);

        response.addCookie(refreshCookie);
        log.debug("JWt Refresh 쿠키 제거");
    }
}
