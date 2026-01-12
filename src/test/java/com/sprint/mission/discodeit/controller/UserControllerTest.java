package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserStatusService userStatusService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private BinaryContentMapper binaryContentMapper;

    @Test
    @DisplayName("사용자 등록 - 성공")
    void createUser_Success() throws Exception {
        // given
        CreateUserRequestDto requestDto = new CreateUserRequestDto("test@naver.com", "testuser","password123");
        UUID userId = UUID.randomUUID();
        UserResponseDto responseDto = new UserResponseDto(
                userId,
                "testuser",
                "test@naver.com",
                null,
                true
        );

        MockMultipartFile userPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                new ObjectMapper().writeValueAsBytes(requestDto)
        );

        MockMultipartFile profilePart = new MockMultipartFile(
                "profile",
                "profile.png",
                "image/png",
                "dummy image content".getBytes()
        );

        // Mapper와 Service Mock 설정
        CreateBinaryContentRequestDto profileRequestDto =
                new CreateBinaryContentRequestDto(
                        profilePart.getOriginalFilename(),
                        profilePart.getSize(),
                        profilePart.getContentType(),
                        profilePart.getBytes()
                );
        when(binaryContentMapper.toRequestDto(any(MultipartFile.class))).thenReturn(profileRequestDto);
        when(userService.create(any(CreateUserRequestDto.class), any(CreateBinaryContentRequestDto.class)))
                .thenReturn(responseDto);

        // when & then
        mockMvc.perform(multipart("/api/users")
                        .file(userPart)
                        .file(profilePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@naver.com"))
                .andExpect(jsonPath("$.online").value(true));
    }

    @Test
    @DisplayName("사용자 등록 - 실패 (잘못된 이메일)")
    void createUser_InvalidEmail_Failure() throws Exception {
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                "invalid-email",
                "testuser",
                "password123"
        );

        MockMultipartFile userPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                "application/json",
                new ObjectMapper().writeValueAsBytes(requestDto)
        );

        MockMultipartFile profilePart = new MockMultipartFile(
                "profile",
                "profile.png",
                "image/png",
                "dummy image content".getBytes()
        );

        mockMvc.perform(multipart("/api/users")
                        .file(userPart)
                        .file(profilePart)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest()) // 400 Bad Request
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message")
                        .value(org.hamcrest.Matchers.containsString("요청 데이터가 유효하지 않습니다.")))
                .andExpect(jsonPath("$.details.email").value("올바른 이메일 형식이 아닙니다."));
    }
}