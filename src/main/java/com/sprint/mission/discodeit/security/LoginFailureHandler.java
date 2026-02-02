package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sprint.mission.discodeit.global.dto.ErrorResponse;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .build();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException{

        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        ErrorCode code = ErrorCode.INVALID_CREDENTIALS;

        ErrorResponse error = new ErrorResponse(
                Instant.now(),
                code.name(),
                code.getMessage(),
                Collections.emptyMap(),
                exception.getClass().getSimpleName(),
                code.getStatus().value()
        );

        objectMapper.writeValue(response.getWriter(), error);
    }
}
