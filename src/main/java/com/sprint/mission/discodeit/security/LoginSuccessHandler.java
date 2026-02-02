package com.sprint.mission.discodeit.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) authentication.getPrincipal();
        UserDto userDto = userDetails.getUserDto();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), userDto);

    }
}
