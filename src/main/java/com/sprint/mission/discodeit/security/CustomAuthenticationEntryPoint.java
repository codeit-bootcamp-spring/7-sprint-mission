package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
// 인증 실패 처리 -> 로그인 하지 않은 사용자가 접근했을 때 실행
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {

        log.warn("커스텀 예외 핸들링 클래스의 메서드 호출!");
        log.warn("메세지: {}", authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json; charset=UTF-8");

        // Map 생성 및 데이터 추가 (JSON을 간편하게 즉석에서 만들자)
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("message", "NO_LOGIN");
        responseMap.put("code", "401");

        // Map을 JSON 문자열로 변환
        String jsonString
            = new ObjectMapper().writeValueAsString(responseMap);

        // JSON 데이터를 응답 객체에 실어서 클라이언트로 바로 응답.
        response.getWriter().write(jsonString);
    }
}
