package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        UserDto userDto = userDetails.getUserDto();

        String accessToken = jwtTokenProvider.createAccessToken(userDto);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDto);

        // "REFRESH_TOKEN"이라는 이름의 쿠키 생성
        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);  // 자바스크립트로 접근 불가 (XSS 방어)
        refreshCookie.setSecure(false);    // HTTPS 환경에서만 전송 (로컬 개발시는 false로 테스트 가능)
        refreshCookie.setPath("/");       // 모든 경로에서 쿠키 전송
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 (초 단위) - 리프레시 토큰 유효기간과 맞춤

        // 응답에 쿠키 추가
        response.addCookie(refreshCookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JwtDto jwtDto = new JwtDto(userDto, accessToken);
        objectMapper.writeValue(response.getWriter(), jwtDto);

        log.info("로그인 성공: {}", userDto.username());
    }
}
