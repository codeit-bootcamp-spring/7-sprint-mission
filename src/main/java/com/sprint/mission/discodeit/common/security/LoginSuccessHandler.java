package com.sprint.mission.discodeit.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;

    public LoginSuccessHandler(UserRepository userRepository, UserStatusRepository userStatusRepository, UserMapper userMapper, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.userStatusRepository = userStatusRepository;
        this.userMapper = userMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        DiscodeitUserDetails principal = (DiscodeitUserDetails) authentication.getPrincipal();
        UUID userId = principal.getUserDto().id();
        User user = userRepository.findById(userId).orElseThrow();
        userStatusRepository.findByUserId(userId)
                .ifPresentOrElse(us -> us.timeUpdated(),
                () -> userStatusRepository.save(new UserStatus(user)));
        UserResponseDto dto = userMapper.toDto(user, true);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        objectMapper.writeValue(response.getWriter(), dto);
    }
}
