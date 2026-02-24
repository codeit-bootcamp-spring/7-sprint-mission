package com.sprint.mission.discodeit.trash;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
//@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        DiscodeitUserDetails userDetails =
            (DiscodeitUserDetails) authentication.getPrincipal();

        UserDto userDto = userDetails.getUser();

        response.setStatus(HttpServletResponse.SC_OK); // 200
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), userDto);
    }
}
