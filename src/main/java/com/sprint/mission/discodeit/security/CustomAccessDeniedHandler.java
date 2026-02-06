package com.sprint.mission.discodeit.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
// 권한 부족 처리 -> 로그인은 했지만, 관리자 페이지에 일반 유저가 접근하는 등의 행동을 했을 때 실행.
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {

        String username = request.getUserPrincipal() != null
            ? request.getUserPrincipal().getName()
            : "anonymous";

        log.warn("Access Denied: User '{}' attempted to access '{}'",
            username, request.getRequestURI());

        // JSON 요청인 경우
        if (request.getHeader("Accept") != null
            && request.getHeader("Accept").contains("application/json")) {

            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            response.getWriter().write("""
                {
                  "status": 403,
                  "error": "FORBIDDEN",
                  "message": "권한이 없습니다."
                }
            """);
        } else {
            // HTML 요청인 경우
            response.sendRedirect("/access-denied");
        }
    }
}