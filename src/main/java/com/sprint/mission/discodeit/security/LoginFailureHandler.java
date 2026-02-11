package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // NOTE: 인증 실패시 응답을 위한 로직 및 반환을 위한 책임 응답담당으로써 해당 로직만 추가, 즉 실질적인 응답 및 핵심 로직은 여기에
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        ErrorCode code = ErrorCode.UNAUTHORIZED;


        ErrorResponse body = new ErrorResponse(
                Instant.now(),
                code.getCode(),
                code.getMessage(),
                Map.of(),
                LoginFailureHandler.class.getSimpleName(),
                code.getStatus().value()
        );

        objectMapper.writeValue(response.getWriter(), body);

    }
}
