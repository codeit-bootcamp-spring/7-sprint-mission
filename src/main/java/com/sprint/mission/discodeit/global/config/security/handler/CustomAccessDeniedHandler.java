package com.sprint.mission.discodeit.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.ErrorResponse;
import com.sprint.mission.discodeit.global.exception.ErrorResponseMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
// 권한 부족 처리 -> 로그인은 했지만, 관리자 페이지에 일반 유저가 접근하는 등의 행동을 했을 때 실행.
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final ErrorResponseMapper errorResponseMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String username = request.getUserPrincipal() != null
                ? request.getUserPrincipal().getName()
                : "anonymous";

        log.info("User {} has been denied", username);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse errorResponse = errorResponseMapper.toErrorResponseDto(
                accessDeniedException,
                ErrorCode.ACCESS_DENIED,
                null
        );

        String body = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(body);
    }
}


