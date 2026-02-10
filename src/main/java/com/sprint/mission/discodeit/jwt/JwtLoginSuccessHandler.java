package com.sprint.mission.discodeit.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper;
    private final JwtRegistry jwtRegistry;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 1. 인증된 사용자 정보(User 엔티티 등) 가져오기
        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        UserDto userDto = userDetails.getUserDto();

        // 2. 토큰 생성 (AccessToken, RefreshToken) + 만료시간까지 GeneratedToken에 담아서
        GeneratedToken generatedToken = jwtTokenProvider.generateAccessToken(userDto);
        String accessToken = generatedToken.getToken();
        Instant accessExpiration = generatedToken.getExpiration();

        GeneratedToken generatedRefreshToken = jwtTokenProvider.generateRefreshToken(userDto);
        String refreshToken = generatedRefreshToken.getToken();
        Instant refreshExpiration = generatedRefreshToken.getExpiration();


        JwtInformation jwtInformation =
                new JwtInformation(userDto,
                        accessToken,
                        refreshToken,
                        accessExpiration,
                        refreshExpiration);

        jwtRegistry.registerJwtInformation(jwtInformation);

        // 3. Refresh Token을 쿠키에 저장
        ResponseCookie refreshTokenCookie = ResponseCookie.from("REFRESH_TOKEN", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtProperties.getRefreshTokenExpiration() / 1000)
                .sameSite("Lax")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());

        // 4. Access Token을 포함한 JwtDto 생성 및 응답 바디 작성
        JwtDto jwtDto = new JwtDto(userDto, accessToken);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String json = objectMapper.writeValueAsString(jwtDto);
        response.getWriter().write(json);
    }
}
