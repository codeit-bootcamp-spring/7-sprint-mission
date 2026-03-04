package com.sprint.mission.discodeit.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class CookieProvider {

    public void setRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        // "REFRESH_TOKEN"이라는 이름의 쿠키 생성
        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(request.isSecure());
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일

        // 응답에 쿠키 추가
        response.addCookie(refreshCookie);
    }
}
