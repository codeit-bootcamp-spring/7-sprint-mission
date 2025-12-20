package com.sprint.mission.discodeit.integration;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.dto.response.UserStatusDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserIntegrationTest extends IntegrationTest{

    @Autowired
    UserRepository userRepository;

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {
        @Test
        @DisplayName("유저 생성 성공")
        void createUser_success() throws Exception {
            //given
            UserCreateRequest userCreateRequest = new UserCreateRequest("test@email.com", "1234", "test");

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

            //when
            mockMvc.perform(multipart("/api/users")
                            .file(profileImage)
                            .file(userCreateRequestPart)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                    )
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.email").value("test@email.com"))
                    .andExpect(jsonPath("$.username").value("test"));
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

            User user = new User("test@email.com", "1234", "test");
            userRepository.save(user);

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
        }

    }

    @Nested
    @DisplayName("유저 조회")
    class GetUser {
        @Test
        @DisplayName("유저 조회 성공")
        void getUser_success() throws Exception {
            //given
            User user = new User("test@email.com", "1234", "test1");
            User user2 = new User("test2@email.com", "1234", "test2");
            userRepository.save(user);
            userRepository.save(user2);


            //when, then
            mockMvc.perform(get("/api/users"))
                    .andDo(print())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].username").value("test1"))
                    .andExpect(jsonPath("$[1].username").value("test2"));
        }

        @Test
        @DisplayName("유저 조회 성공2")
        void getUser_success2() throws Exception {
            //when
            mockMvc.perform(get("/api/users"))
                    .andDo(print())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("유저 삭제")
    class DeleteUser {
        @Test
        @DisplayName("유저 삭제 성공")
        void deleteUser_success() throws Exception {
            // given
            User user = new User("test@email.com", "1234", "test1");
            User save = userRepository.save(user);


            // when & then
            mockMvc.perform(delete("/api/users/{userId}", save.getId()))
                    .andDo(print());
        }
        @Test
        @DisplayName("유저 삭제 실패")
        void deleteUser_fail() throws Exception {
            // given
            UUID userId = UUID.randomUUID();

            // when & then
            mockMvc.perform(delete("/api/users/{userId}", userId))
                    .andExpect(jsonPath("$.code").value("USER_NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("해당 유저를 찾을 수 없습니다."));
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {
        @Test
        @DisplayName("유저 수정 성공")
        void updateUser_success() throws Exception {
            // given
            User user = new User("test@email.com", "1234", "test1");
            User save = userRepository.save(user);

            UserUpdateRequest userUpdateRequest =
                    new UserUpdateRequest("new@gmail.com", "newName", "4321");


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
                            multipart("/api/users/{userId}", save.getId())
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
                    .andExpect(jsonPath("$.id").value(save.getId().toString()))
                    .andExpect(jsonPath("$.username").value("newName"))
                    .andExpect(jsonPath("$.email").value("new@gmail.com"));
        }

        @Test
        @DisplayName("유저 수정 실패 - 잘못된 요청 값")
        void updateUser_fail_invalidRequest() throws Exception {
            // given
            UserUpdateRequest userUpdateRequest =
                    new UserUpdateRequest("not-an-email", "", "4321");

            MockMultipartFile userUpdateRequestPart = new MockMultipartFile(
                    "userUpdateRequest",
                    "",
                    "application/json",
                    objectMapper.writeValueAsBytes(userUpdateRequest)
            );

            // when & then
            mockMvc.perform(
                            multipart("/api/users/{userId}", UUID.randomUUID())
                                    .file(userUpdateRequestPart)
                                    .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    })
                                    .contentType(MediaType.MULTIPART_FORM_DATA)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }


    @Nested
    @DisplayName("유저 상태")
    class UserStatus {
        @Test
        @DisplayName("유저 상태 업데이트 성공")
        void markOnline_success() throws Exception {
            //given
            User user = new User("test@email.com", "1234", "test1");
            User save = userRepository.save(user);


            Instant lastActiveAt = Instant.now();
            UserStatusUpdateRequest userStatusUpdateRequest = new UserStatusUpdateRequest(lastActiveAt);

            //when
            mockMvc.perform(patch("/api/users/{userId}/userStatus", save.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(userStatusUpdateRequest)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.userId").value(save.getId().toString()))
                    .andExpect(jsonPath("$.lastActiveAt").exists());
        }

        @Test
        @DisplayName("유저 상태 업데이트 실패 - body가 없음")
        void updateUserStatus_fail() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            // when & then
            mockMvc.perform(
                            patch("/api/users/{userId}/userStatus", userId)
                    )
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
