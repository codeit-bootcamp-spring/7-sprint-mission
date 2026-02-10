package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.JwtDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();

        String accessToken = jwtTokenProvider.createAccessToken(principal);
        JwtTokenProvider.RefreshTokenResult refresh = jwtTokenProvider.createRefreshToken(principal);

        refreshTokenService.upsert(principal.getUser().getId(), refresh.tokenId(), refresh.expiresAt());

        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refresh.token());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) jwtTokenProvider.getRefreshTokenMaxAgeSeconds());
        response.addCookie(refreshCookie);

        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), new JwtDto(principal.getUserDto(), accessToken));
    }
}
