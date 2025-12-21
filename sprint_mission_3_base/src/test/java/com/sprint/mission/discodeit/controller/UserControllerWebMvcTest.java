package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.config.WebMvcTestSupportConfig;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class
        }
)
@Import(WebMvcTestSupportConfig.class)
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
    @DisplayName("POST /api/users 성공: 유저 생성(멀티파트) -> 201 + JSON")
    void create_success_multipart() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDto response = new UserDto(
                userId,
                "taehun",
                "t@e.com",
                null
        );

        given(userService.create(any(), any())).willReturn(response);

        MockMultipartFile profile = new MockMultipartFile(
                "profile",
                "profile.png",
                "image/png",
                "x".getBytes()
        );

        MockMultipartFile userCreateRequest = new MockMultipartFile(
                "userCreateRequest",
                "userCreateRequest.json",
                "application/json",
                """
                {
                  "username": "taehun",
                  "email": "t@e.com",
                  "password": "password1234"
                }
                """.getBytes()
        );

        mockMvc.perform(
                        multipart("/api/users")
                                .file(profile)
                                .file(userCreateRequest)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("taehun"))
                .andExpect(jsonPath("$.email").value("t@e.com"));
    }

    @Test
    @DisplayName("POST /api/users 실패: Validation 실패 -> 400 + fieldErrors")
    void create_validation_fail() throws Exception {
        MockMultipartFile userCreateRequest = new MockMultipartFile(
                "userCreateRequest",
                "userCreateRequest.json",
                "application/json",
                """
                {
                  "username": "",
                  "email": "not-an-email",
                  "password": ""
                }
                """.getBytes()
        );

        mockMvc.perform(multipart("/api/users").file(userCreateRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.fieldErrors").exists());
    }

    @Test
    @DisplayName("DELETE /api/users/{id} 실패: USER_NOT_FOUND -> 404 + ErrorResponse")
    void delete_not_found() throws Exception {
        UUID userId = UUID.randomUUID();

        willThrow(new DiscodeitException(ErrorCode.USER_NOT_FOUND))
                .given(userService)
                .delete(eq(userId));

        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.name()));
    }
}
