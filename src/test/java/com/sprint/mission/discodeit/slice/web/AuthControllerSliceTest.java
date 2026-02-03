package com.sprint.mission.discodeit.slice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.SecurityConfig;
import com.sprint.mission.discodeit.controller.AuthController;
import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.service.AuthService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@Import(SecurityConfig.class) // NOTE: 슬라이스 테스트기때문에 설정대로 나오기위해 import 해줌
public class AuthControllerSliceTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    AuthService authService;

    @Nested
    class Login {

        @Test
        void returns400_when_username_blank() throws Exception {
            Map<String, String> body = Map.of(
                    "username", " ",
                    "password", "pw"
            );

            mockMvc.perform(post("/api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(authService, never()).login(any(AuthLoginRequestDto.class));
        }

        @Test
        void returns400_when_password_blank() throws Exception {
            Map<String, String> body = Map.of(
                    "username", "user",
                    "password", " "
            );

            mockMvc.perform(post("/api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            verify(authService, never()).login(any(AuthLoginRequestDto.class));
        }

        @Test
        void returns200_and_calls_service_when_valid() throws Exception {
            Map<String, String> body = Map.of(
                    "username", "user",
                    "password", "pw"
            );

            UUID id = UUID.randomUUID();

            when(authService.login(any(AuthLoginRequestDto.class)))
                    .thenReturn(new UserResponseDto(
                            id,
                            "user",
                            "test@test.com",
                            null,
                            true,
                            Instant.now(),
                            Instant.now()
                    ));

            mockMvc.perform(post("/api/auth/login")
                            .with(csrf())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(id.toString()))
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.email").value("test@test.com"));


            verify(authService, times(1)).login(any(AuthLoginRequestDto.class));
        }
    }
}
