package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@DisplayName("UserController Test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private UserStatusService userStatusService;

    @Nested
    @DisplayName("유저 조회")
    class GetUser {

        @Test
        @DisplayName("ID로 유저 1명 조회할 수 있다.")
        void getUser() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            UserDto userDto = UserDto.builder()
                    .id(userId)
                    .username("user")
                    .email("user@codeit.com")
                    .build();
            when(userService.findUserById(userId)).thenReturn(userDto);

            // when & then

            mockMvc.perform(get("/api/users/" + userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("user"))
                    .andExpect(jsonPath("$.email").value("user@codeit.com"));
        }

        @Test
        @DisplayName("유저를 전체 조회할 수 있다.")
        void getAllUsers() throws Exception {
            // given
            UserDto user1 = UserDto.builder()
                    .username("user1")
                    .email("user1@codeit.com")
                    .build();

            UserDto user2 = UserDto.builder()
                    .username("user2")
                    .email("user2@codeit.com")
                    .build();

            when(userService.findAllUsers()).thenReturn(List.of(user1, user2));

            // when & then
            mockMvc.perform(get("/api/users"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].username").value("user1"))
                    .andExpect(jsonPath("$.[0].email").value("user1@codeit.com"))
                    .andExpect(jsonPath("$.[1].username").value("user2"))
                    .andExpect(jsonPath("$.[1].email").value("user2@codeit.com"));
        }

        @Test
        @DisplayName("존재하지 않는 유저를 조회하면 404를 반환한다.")
        void getUserById_NotFound() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            when(userService.findUserById(userId)).thenThrow(new UserNotFoundException(userId));

            // when & then
            mockMvc.perform(get("/api/users/" + userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
                    .andExpect(jsonPath("$.code").value("U001"));
        }
    }

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {

        @Test
        @DisplayName("유저를 생성할 수 있다.")
        void createUser_Success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            UserCreateRequest request = UserCreateRequest.builder().build();
            UserDto response = UserDto.builder().id(userId).build();
            when(userService.createUser(any(UserCreateRequest.class), isNull())).thenReturn(response);

            String json = objectMapper.writeValueAsString(request);
            MockMultipartFile multipartFile
                    = new MockMultipartFile("userCreateRequest", "",
                    MediaType.APPLICATION_JSON_VALUE, json.getBytes());

            // when & then
            mockMvc.perform(multipart("/api/users")
                            .file(multipartFile)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location")) // header에 Location이라는 정보가 있을 때
                    .andExpect(header().string("Location", "/api/users/" + userId));
        }
    }

    @Nested
    @DisplayName("유저 정보 수정")
    class updateUser {

        @Test
        @DisplayName("유저 정보를 수정할 수 있다.")
        void updateUser_Success() throws Exception {
            UUID userId = UUID.randomUUID();
            UserUpdateRequest request = new UserUpdateRequest("new", "new@codeit.com", null);
            UserDto response = UserDto.builder().id(userId).username("new").email("new@codeit.com").build();
            when(userService.updateUserInfo(eq(userId), any(UserUpdateRequest.class), isNull()))
                    .thenReturn(response);

            String json = objectMapper.writeValueAsString(request);
            MockMultipartFile multipartFile
                    = new MockMultipartFile("userUpdateRequest", "",
                    MediaType.APPLICATION_JSON_VALUE, json.getBytes());

            // when & then
            mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/" + userId)
                            .file(multipartFile)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value("new"))
                    .andExpect(jsonPath("$.email").value("new@codeit.com"));
        }

        @Test
        @DisplayName("존재하지 않는 유저를 수정하면 404를 반환한다.")
        void updateUser_NotFound() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            when(userService.updateUserInfo(eq(userId), any(), isNull()))
                    .thenThrow(new UserNotFoundException(userId));

            MockMultipartFile multipartFile
                    = new MockMultipartFile("userUpdateRequest", "",
                    MediaType.APPLICATION_JSON_VALUE, "{}".getBytes());

            // when & then
            mockMvc.perform(multipart(HttpMethod.PATCH, "/api/users/" + userId)
                            .file(multipartFile)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
                    .andExpect(jsonPath("$.code").value("U001"));
        }
    }

    @Nested
    @DisplayName("유저 삭제")
    class DeleteUser {

        @Test
        @DisplayName("유저를 삭제할 수 있다.")
        void deleteUser_Success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();

            // when & then
            mockMvc.perform(delete("/api/users/" + userId))
                    .andExpect(status().isNoContent());
            verify(userService).deleteUser(userId);
        }

        @Test
        @DisplayName("존재하지 않는 유저 삭제 시 404를 반환한다.")
        void deleteUser_NotFound() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
//            when(userService.findUserById(userId)).thenThrow(new UserNotFoundException(userId));
            doThrow(new UserNotFoundException(userId)).when(userService).deleteUser(userId);

            // when & then
            mockMvc.perform(delete("/api/users/" + userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."))
                    .andExpect(jsonPath("$.code").value("U001"));
        }
    }
}