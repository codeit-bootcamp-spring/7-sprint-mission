package com.sprint.mission.discodeit.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtDto;
import com.sprint.mission.discodeit.global.config.security.jwt.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        UserResponseDto userResponseDto = userDetails.getUserResponseDto();
        String username = userResponseDto.username();

        String accessToken = jwtProvider.generateAccessToken(username);
        String refreshToken = jwtProvider.generateRefreshToken(username);

        JwtDto jwtDto = new JwtDto(userResponseDto, accessToken);
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        Cookie refreshCookie = new Cookie(JwtProvider.REFRESH_COOKIE_NAME, refreshToken);

        refreshCookie.setHttpOnly(true);  // 자바스크립트로 접근 불가 (XSS 방어)
        refreshCookie.setSecure(request.isSecure());    // HTTPS 환경에서만 전송 (로컬 개발시는 false로 테스트 가능)
        refreshCookie.setPath("/");       // 모든 경로에서 쿠키 전송
        refreshCookie.setMaxAge((int) (jwtProvider.getRefreshTokenExpirationMills() / 1000));
        response.addCookie(refreshCookie); // 응답에 쿠키 추가

        response.getWriter().write(objectMapper.writeValueAsString(jwtDto));
    }
}
