package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@WebMvcTest(UserController.class)
@DisplayName("userController 테스트")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {
        @Test
        @DisplayName("유저 생성 성공")
        void createUser_success() throws Exception {
            //given
            UserCreateRequest userCreateRequest = new UserCreateRequest("test@email.com", "1234", "test");
            UserDto response = new UserDto();
            response.setEmail(userCreateRequest.email());
            UUID userId = UUID.randomUUID();
            response.setId(userId);
            response.setUsername(userCreateRequest.username());

            MockMultipartFile userCreateRequestPart = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    "application/json",
                    objectMapper.writeValueAsBytes(userCreateRequest)
            );

            MockMultipartFile profileImage = new MockMultipartFile(
                    "profileImage",
                    "test.png",
                    "image/png",
                    "fake image data".getBytes()
            );
            given(userService.createUser(userCreateRequest, profileImage))
                    .willReturn(response);

            //when
            mockMvc.perform(multipart("/api/users")
                            .file(profileImage)
                            .file(userCreateRequestPart)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(userId.toString()))
                    .andExpect(jsonPath("$.username").value(userCreateRequest.username()));

            //then
            then(userService).should().createUser(userCreateRequest, profileImage);
        }

        @Test
        @DisplayName("유저 생성 실패 - 이미 존재하는 이메일")
        void createUser_fail_duplicateEmail() throws Exception {
            // given
            UserCreateRequest userCreateRequest =
                    new UserCreateRequest("test@email.com", "1234", "test");

            MockMultipartFile userCreateRequestPart = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    "application/json",
                    objectMapper.writeValueAsBytes(userCreateRequest)
            );

            given(userService.createUser(any(UserCreateRequest.class), isNull()))
                    .willThrow(new UserAlreadyExistsException(ErrorCode.DUPLICATE_USER, new HashMap<>()));

            // when & then
            mockMvc.perform(
                            multipart("/api/users")
                                    .file(userCreateRequestPart)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(jsonPath("$.code").value("DUPLICATE_USER"))
                    .andExpect(jsonPath("$.message").value("이미 존재하는 유저입니다."))
                    .andExpect(status().isBadRequest());


            then(userService).should()
                    .createUser(any(UserCreateRequest.class), isNull());
        }
    }

    @Nested
    @DisplayName("유저 조회")
    class GetUser {
        @Test
        @DisplayName("유저 조회 성공")
        void getUser_success() throws Exception {
            //given
            UserDto userDto = new UserDto();
            userDto.setUsername("test1");
            UserDto userDto2 = new UserDto();
            userDto2.setUsername("test2");

            given(userService.getAllUsers())
                    .willReturn(List.of(userDto, userDto2));

            //when
            mockMvc.perform(get("/api/users"))
                    .andDo(print())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].username").value("test1"))
                    .andExpect(jsonPath("$[1].username").value("test2"));

            //then
            then(userService).should().getAllUsers();
        }

        @Test
        @DisplayName("유저 조회 성공2")
        void getUser_success2() throws Exception {
            //given
            given(userService.getAllUsers())
                    .willReturn(List.of());

            //when
            mockMvc.perform(get("/api/users"))
                    .andDo(print())
                    .andExpect(jsonPath("$", hasSize(0)));

            //then
            then(userService).should().getAllUsers();
        }
    }

    @Nested
    @DisplayName("유저 삭제")
    class DeleteUser {
        @Test
        @DisplayName("유저 삭제 성공")
        void deleteUser_success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();

            willDoNothing()
                    .given(userService)
                    .deleteUser(userId);

            // when & then
            mockMvc.perform(delete("/api/users/{userId}", userId))
                    .andExpect(status().isNoContent());

            then(userService).should()
                    .deleteUser(userId);
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {
        @Test
        @DisplayName("유저 수정 성공")
        void updateUser_success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();

            UserUpdateRequest userUpdateRequest =
                    new UserUpdateRequest("new@gmail.com", "newName", "4321");

            UserDto response = new UserDto();
            response.setId(userId);
            response.setUsername("newName");
            response.setEmail("new@gmail.com");

            MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
                    "userUpdateRequest",
                    "",
                    "application/json",
                    objectMapper.writeValueAsBytes(userUpdateRequest)
            );

            MockMultipartFile profileImage = new MockMultipartFile(
                    "profile",
                    "profile.png",
                    "image/png",
                    "fake image data".getBytes()
            );

            given(userService.updateUserInfo(userId, userUpdateRequest, profileImage))
                    .willReturn(response);

            // when & then
            mockMvc.perform(
                            multipart("/api/users/{userId}", userId)
                                    .file(userUpdateRequestPart)
                                    .file(profileImage)
                                    .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    })
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId.toString()))
                    .andExpect(jsonPath("$.username").value("newName"))
                    .andExpect(jsonPath("$.email").value("new@gmail.com"));

            then(userService).should()
                    .updateUserInfo(userId, userUpdateRequest, profileImage);
        }

        @Test
        @DisplayName("유저 수정 실패 - 잘못된 요청 값")
        void updateUser_fail() throws Exception {
            // given
            UUID userId = UUID.randomUUID();

            UserUpdateRequest userUpdateRequest =
                    new UserUpdateRequest("not-an-email", "", "4321");

            MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
                    "userUpdateRequest",
                    "",
                    "application/json",
                    objectMapper.writeValueAsBytes(userUpdateRequest)
            );

            MockMultipartFile profileImage = new MockMultipartFile(
                    "profile",
                    "profile.png",
                    "image/png",
                    "fake image data".getBytes()
            );

            // when & then
            mockMvc.perform(
                            multipart("/api/users/{userId}", userId)
                                    .file(userUpdateRequestPart)
                                    .file(profileImage)
                                    .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    })
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(userService).shouldHaveNoInteractions();
        }
    }


    @Nested
    @DisplayName("유저 상태")
    class UserStatus {
        @Test
        @DisplayName("유저 상태 업데이트 성공")
        void markOnline_success() throws Exception {
            //given
            UUID userId = UUID.randomUUID();
            Instant lastActiveAt = Instant.now();
            UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(lastActiveAt);

            UserStatusDto response = new UserStatusDto();
            response.setUserId(userId);
            response.setLastActiveAt(lastActiveAt);

            given(userService.updateLastActiveAt(userId, lastActiveAt))
                    .willReturn(response);

            //when
            mockMvc.perform(patch("/api/users/{userId}/userStatus", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userStatusUpdateRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(userId.toString()))
                    .andExpect(jsonPath("$.lastActiveAt").exists());

            //then
            then(userService).should()
                    .updateLastActiveAt(userId, lastActiveAt);
        }

        @Test
        @DisplayName("유저 상태 업데이트 실패")
        void updateUserStatus_fail() throws Exception {
            // given
            UUID userId = UUID.randomUUID();

            // when & then
            mockMvc.perform(
                            patch("/api/users/{userId}/userStatus", userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            then(userService).shouldHaveNoInteractions();
        }
    }
}
