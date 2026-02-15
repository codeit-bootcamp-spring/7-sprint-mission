package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.mapper.dto.JwtDto;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final String REFRESH_TOKEN_COOKIE = "REFRESH_TOKEN";

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) throws IOException, ServletException {

        // 1️⃣ 인증된 사용자 정보
        DiscodeitUserDetails principal =
            (DiscodeitUserDetails) authentication.getPrincipal();

        String username = principal.getUser().username();
        UUID userId = principal.getUser().id();
        Role role = principal.getUser().role();

        // 2️⃣ 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(username, role.name());
        String refreshToken = jwtTokenProvider.createRefreshToken(username);

        // 3️⃣ Refresh Token → Cookie (HttpOnly + SameSite)
        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_COOKIE, refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(request.isSecure());          // true == HTTPS 환경. so 로컬(http) 에서 테스트 할 경우 쿠키 안내려옴!
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(60 * 60 * 24 * 14); // 14일

        response.addCookie(refreshCookie);

        // 4️⃣ UserDto 생성
        UserDto userDto = UserDto.builder()
            .id(userId)
            .username(principal.getUsername())
            .role(role)
            .build();

        // 5️⃣ JwtDto 응답
        JwtDto jwtDto = JwtDto.builder()
            .userDto(userDto)
            .accessToken(accessToken)
            .build();

        // CSRF 방어 = SameSite=Strict : 가장 엄격. 같은 사이트에서만 쿠키 전송 가능
        response.addHeader(
            "Set-Cookie",
            "REFRESH_TOKEN=" + refreshToken +
                "; Path=/; HttpOnly; Secure; SameSite=Strict"
        );
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");


        objectMapper.writeValue(response.getWriter(), jwtDto);
    }
}
