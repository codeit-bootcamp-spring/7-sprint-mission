package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // NOTE: 인증 성공 시 응답을 위한 로직 및 반환을 위한 책임 응답담당으로써 해당로직만 추가, 즉 실질적인 응답 및 핵심 로직은 여기에

        HttpSession session = request.getSession(); // 세션 없으면 생성
        session.setAttribute("LOGIN_TIME", Instant.now());
        session.setAttribute("LOGIN_IP", request.getRemoteAddr());
        session.setAttribute("USER_AGENT", request.getHeader("User-Agent"));

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();

        UserResponseDto userResponseDto = userDetails.getUserResponseDto();


        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), userResponseDto);
    }
}
