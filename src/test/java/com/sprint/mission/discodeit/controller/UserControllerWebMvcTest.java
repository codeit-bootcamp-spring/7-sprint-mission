package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.common.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class UserControllerWebMvcTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    @MockitoBean
    UserStatusService userStatusService;

    @Test
    @DisplayName("POST: /api/users (multipart) 성공")
    void createMultipart_success() throws Exception {
        // given
        UUID userId = UUID.randomUUID();
        UserCreateRequestDto userCreateRequestDto
                = new UserCreateRequestDto("user", "password1234", "user@naver.com");

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "userCreateRequest",
                "userCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(userCreateRequestDto));

        UserResponseDto userResponseDto
                = new UserResponseDto(userId, "user", "user@naver.com", null, true);

        given(userService.create(any(UserCreateRequestDto.class), isNull())).willReturn(userResponseDto);

        // when & then
        mockMvc.perform(
                multipart("/api/users")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.email").value("user@naver.com"));

    }

    @Test
    @DisplayName("POST: /api/users (multipart) 실패")
    void createMultipart_fail() throws Exception {
        // given
        UserCreateRequestDto userCreateRequestDto
                = new UserCreateRequestDto("", "password1234", "user@naver.com");

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "userCreateRequest",
                "userCreateRequest",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(userCreateRequestDto)
        );

        // when & then
        mockMvc.perform(
                multipart("/api/users")
                        .file(mockMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
        )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.details.fieldErrors").exists())
                .andExpect(jsonPath("$.details.fieldErrors.username").exists());


    }
}
