package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.global.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String username = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : "anonymous";

        log.warn("Access Denied: User {} attempted to access {}",
                username, request.getRequestURI());

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json; charset=UTF-8");

        String message = "권한이 없습니다.";
        String requestUri = request.getRequestURI();

        if (requestUri.contains("/channels")) {
            message = "채널 관리자(CHANNEL_MANAGER) 권한이 필요합니다.";
        } else if (requestUri.contains("/auth/role")) {
            message = "관리자(ADMIN) 권한이 필요합니다.";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                Instant.now(),
                "FORBIDDEN",
                message,
                Collections.emptyMap(),
                accessDeniedException.getClass().getSimpleName(),
                HttpStatus.FORBIDDEN.value()
        );

        String json = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(json);
    }
}
