package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final AuthService authService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        if (request.getCookies() == null) return;
        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("REFRESH_TOKEN"))
                .findFirst()
                .ifPresent(cookie ->
                        authService.expireUserSession(cookie.getValue())
                );

        Cookie cookie = new Cookie("REFRESH_TOKEN", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("JWT 로그아웃: Refresh Token 삭제 완료");
    }
}
