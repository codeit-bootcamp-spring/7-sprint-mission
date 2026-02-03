package com.sprint.mission.discodeit.slice.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.SecurityConfig;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserSignupCommand;
import com.sprint.mission.discodeit.dto.user.UserSignupRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerSliceTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    private MockMultipartFile userPart(UserSignupRequestDto dto) throws Exception {
        return new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(dto)
        );
    }

    private MockMultipartFile validUserPart() throws Exception {
        return userPart(UserSignupRequestDto.builder()
                .username("user")
                .email("user@test.com")
                .password("pw1234")
                .build());
    }

    @Nested
    class CreateUserValidation {

        @Test
        void returns400_when_username_blank() throws Exception {
            UserSignupRequestDto dto = UserSignupRequestDto.builder()
                    .username(" ")
                    .email("user@test.com")
                    .password("pw")
                    .build();

            mockMvc.perform(multipart("/api/users")
                            .file(userPart(dto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns400_when_email_invalid() throws Exception {
            UserSignupRequestDto dto = UserSignupRequestDto.builder()
                    .username("user")
                    .email("invalid-email")
                    .password("pw")
                    .build();

            mockMvc.perform(multipart("/api/users")
                            .file(userPart(dto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void returns400_when_password_blank() throws Exception {
            UserSignupRequestDto dto = UserSignupRequestDto.builder()
                    .username("user")
                    .email("user@test.com")
                    .password(" ")
                    .build();

            mockMvc.perform(multipart("/api/users")
                            .file(userPart(dto)))
                    .andExpect(status().isBadRequest());
        }
    }
    @Nested
    @DisplayName("signup")
    class UserSignup {

        @Test
        void signup_then_success() throws Exception {
            // given
            final String uri = "/api/users";
            UUID expectedId = UUID.randomUUID();
            User user = User.create("test22", "emaile@edsd.com", "password", null);

            // dto
            UserSignupRequestDto request = UserSignupRequestDto.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .build();
            String requestJson = objectMapper.writeValueAsString(request);

            MockMultipartFile multipartFile = new MockMultipartFile(
                    "userCreateRequest",
                    "userCreateRequest.json",
                    MediaType.APPLICATION_JSON_VALUE,
                    requestJson.getBytes(StandardCharsets.UTF_8)
            );


            when(userService.signUp(any())).thenReturn(new UserResponseDto(
                    expectedId,
                    user.getUsername(),
                    user.getEmail(),
                    null,
                    true,
                    user.getCreatedAt(),
                    user.getUpdatedAt()
            ));

            // when
            ResultActions actions = mockMvc.perform(
                    multipart(uri)
                            .file(multipartFile)
                            .with(csrf())
                            .characterEncoding("UTF-8")
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .accept(MediaType.APPLICATION_JSON)

            );


            actions
                    .andExpect(status().isOk())
                    .andExpect(result -> {
                        String responseBody = result.getResponse().getContentAsString();
                        assertNotNull(responseBody);
                    })
                    .andExpect(jsonPath("$.id").value(expectedId.toString()))
                    .andExpect(jsonPath("$.username").value(user.getUsername()))
                    .andExpect(jsonPath("$.email").value(user.getEmail()));
        }


    }


    @Nested
    class CreateUserSuccess {

        @Test
        void returns201_when_valid_without_profile() throws Exception {
            UUID userId = UUID.randomUUID();

            when(userService.signUp(any(UserSignupCommand.class)))
                    .thenReturn(new UserResponseDto(
                            userId,
                            "user",
                            "user@test.com",
                            null,
                            true,
                            Instant.now(),
                            Instant.now()
                    ));

            mockMvc.perform(multipart("/api/users")
                            .file(validUserPart()))
                    .andExpect(status().isCreated());

            verify(userService, times(1)).signUp(any(UserSignupCommand.class));
        }

        @Test
        void returns201_when_valid_with_profile() throws Exception {
            UUID userId = UUID.randomUUID();

            MockMultipartFile profile = new MockMultipartFile(
                    "profile",
                    "profile.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "image".getBytes()
            );

            when(userService.signUp(any(UserSignupCommand.class)))
                    .thenReturn(new UserResponseDto(
                            userId,
                            "user",
                            "user@test.com",
                            null,
                            true,
                            Instant.now(),
                            Instant.now()
                    ));

            mockMvc.perform(multipart("/api/users")
                            .file(validUserPart())
                            .file(profile))
                    .andDo(print())
                    .andExpect(status().isCreated());

            ArgumentCaptor<UserSignupCommand> captor = ArgumentCaptor.forClass(UserSignupCommand.class);

            verify(userService).signUp(captor.capture());
            UserSignupCommand cmd = captor.getValue();
            assertTrue(cmd.profile().isPresent());
            BinaryContentUploadCommand profileCmd = cmd.profile().get();

            assertNotNull(cmd.profile());
            assertEquals("profile.png", profileCmd.fileName());
            assertEquals("image/png", profileCmd.contentType());
            assertTrue(profileCmd.bytes().length > 0);
        }
    }
}
