package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "anonymous";

        log.warn("Access Denied: User ={} attempted to access : method={}, uri={}",
                username, request.getMethod(), request.getRequestURI());


        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorCode code = ErrorCode.FORBIDDEN;

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                code.getCode(),
                code.getMessage(),
                Map.of(),
                CustomAccessDeniedHandler.class.getSimpleName(),
                code.getStatus().value()
        );

        objectMapper.writeValue(response.getWriter(), body);

    }
}
