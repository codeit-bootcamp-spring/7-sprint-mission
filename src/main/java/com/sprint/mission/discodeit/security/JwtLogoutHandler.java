package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.service.JwtRegistry;
import com.sprint.mission.discodeit.service.UserService;
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

    private final JwtRegistry jwtRegistry;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        if (request.getCookies() == null) return;
        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("REFRESH_TOKEN"))
                .findFirst()
                .ifPresent(cookie -> {
                    String refreshToken = cookie.getValue();

                    if (jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)){
                        String username = jwtTokenProvider.getUsername(refreshToken);
                        UserDto userdto = userService.findUserByUsername(username);
                        jwtRegistry.invalidateJwtInformationByUserId(userdto.id());
                    }
                });

        Cookie cookie = new Cookie("REFRESH_TOKEN", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        log.info("JWT 로그아웃: Refresh Token 삭제 완료");
    }
}
