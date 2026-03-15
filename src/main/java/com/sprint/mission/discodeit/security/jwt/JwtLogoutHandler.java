package com.sprint.mission.discodeit.security.jwt;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.event.user.UserUpdatedEvent;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final RefreshTokenService refreshTokenService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return;
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();

        UserResponseDto userResponseDto = userDetails.getUserResponseDto();
        eventPublisher.publishEvent(new UserUpdatedEvent(userResponseDto.id()));

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
