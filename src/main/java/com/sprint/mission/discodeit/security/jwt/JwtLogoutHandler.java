package com.sprint.mission.discodeit.security.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

@Component
public class JwtLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        Cookie cookie = new Cookie("REFRESH_TOKEN", "");
        cookie.setPath("/");        // 생성 시와 동일하게
        cookie.setMaxAge(0);        // 즉시 삭제
        cookie.setHttpOnly(true);
        cookie.setSecure(false);     // 생성 시와 동일하게

        response.addCookie(cookie);
    }
}
