package com.sprint.mission.discodeit.security;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import com.sprint.mission.discodeit.mapper.dto.JwtDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (authentication.getPrincipal() instanceof DiscodeitUserDetails userDetails) {
            try {
                String accessToken = tokenProvider.generateAccessToken(userDetails);
                String refreshToken = tokenProvider.generateRefreshToken(userDetails);

                // Set refresh token in HttpOnly cookie
                Cookie refreshCookie = tokenProvider.genereateRefreshTokenCookie(refreshToken);
                response.addCookie(refreshCookie);

                JwtDto jwtDto = new JwtDto(
                    userDetails.getUserDto(),
                    accessToken
                );

                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(objectMapper.writeValueAsString(jwtDto));

                log.info("JWT access and refresh tokens issued for user: {}", userDetails.getUsername());

            } catch (JOSEException e) {
                log.error("🚨Failed to generate JWT token for user: {}", userDetails.getUsername(), e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                ErrorResponse errorResponse = new ErrorResponse(
                    Instant.now(),
                    "500",
                    "🚨Failed to generate JWT token_500",
                    Map.of(),
                    "RuntimeException",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
                );

                response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                "AUTH_401",
                "🚨Authentication failed: Invalid user details",
                Map.of(),
                "RuntimeException",
                HttpServletResponse.SC_UNAUTHORIZED
            );
            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
        }
    }

}
