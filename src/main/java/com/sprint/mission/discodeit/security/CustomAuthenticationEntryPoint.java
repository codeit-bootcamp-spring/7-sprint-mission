package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint { // NOTE: 인증되지 않고 접근시 처리를 위한 엔트리포인트

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.warn("인증되지 않은 요청이 보호된 리소스에 접근함 (AuthenticationEntryPoint)");
        log.warn("Unauthorized access: method={}, uri={}",
                request.getMethod(),
                request.getRequestURI());
        log.warn("메세지: {}", authException.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ErrorCode code = ErrorCode.UNAUTHORIZED;

        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                code.getCode(),
                code.getMessage(),
                Map.of(),
                CustomAuthenticationEntryPoint.class.getSimpleName(),
                code.getStatus().value()
        );

        objectMapper.writeValue(response.getWriter(), body);
    }
}
