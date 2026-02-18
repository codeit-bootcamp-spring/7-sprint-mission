package com.sprint.mission.discodeit.global.config.security.handler;

import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtRegistry;
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
@RequiredArgsConstructor
@Slf4j
public class JwtLogOutHandler implements LogoutHandler {
    private final JwtProvider jwtProvider;
    private final JwtRegistry jwtRegistry;
    private final DiscodeitUserDetailsService userDetailsService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Cookie refreshCookie = new Cookie(JwtProvider.REFRESH_COOKIE_NAME, null);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(false);
        refreshCookie.setMaxAge(0);

        response.addCookie(refreshCookie);

        Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(JwtProvider.REFRESH_COOKIE_NAME))
                .findFirst()
                .ifPresent(cookie -> {
                    String refreshToken = cookie.getValue();
                    String username = jwtProvider.extractSubject(refreshToken);
                    DiscodeitUserDetails userDetails =
                            (DiscodeitUserDetails) userDetailsService.loadUserByUsername(username);

                    // 5. Registry에서 해당 유저의 정보 삭제
                    jwtRegistry.invalidateJwtInformationByUserId(userDetails.getUserResponseDto().id());

                });

        log.debug("JWt Refresh 쿠키 제거");
    }
}
