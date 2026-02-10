package com.sprint.mission.discodeit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.fixture.user.dto.CreateUserDtoFixture;
import com.sprint.mission.discodeit.fixture.user.dto.UserResponseDtoFixture;
import com.sprint.mission.discodeit.global.exception.ErrorResponseMapperImpl;
import com.sprint.mission.discodeit.global.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @WebMvcTest는 기본적으로 컨트롤러 레이어만 로드
 * 하지만 테스트에 필요한 다른 빈들은 자동으로 로드되지 않음
 * @Import로 필요한 빈을 명시적으로 추가
 */
@WebMvcTest(UserController.class)
@Import({GlobalExceptionHandler.class, ErrorResponseMapperImpl.class})
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("User Controller 테스트")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc; // HTTP 요청 시뮬레이션

    @Autowired
    private ObjectMapper objectMapper; // JSON 변환용

    @MockitoBean
    private UserService userService; // 서비스 레이어 모킹

    @Nested
    @DisplayName("유저 생성 테스트")
    class UserCreate {
        @Test
        @DisplayName("[정상 케이스] - 유저 생성 성공 - 프로필 없음")
        void createUser_notProfile_success() throws Exception {
            // given
            CreateUserDto createUserDto = CreateUserDtoFixture.createUserDto();
            UUID userId = UUID.randomUUID();
            UserResponseDto userResponseDto = UserResponseDtoFixture.createUserResponse(userId, createUserDto.email(), createUserDto.username());

            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createUserDto)
            );

            given(userService.createUser(any(CreateUserDto.class), argThat(Optional::isEmpty)))
                    .willReturn(userResponseDto);

            // when & then
            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                    )

                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(userId.toString()))
                    .andExpect(jsonPath("$.username").value("test"));


            then(userService).should().createUser(any(), argThat(Optional::isEmpty));
        }

        @Test
        @DisplayName("[정상 케이스] - 유저 생성 성공 - 프로필 포함")
        void createUser_withProfile_success() throws Exception {
            // given
            BinaryContentResponseDto binaryContentResponseDto = new BinaryContentResponseDto(
                    UUID.randomUUID(),
                    Instant.now(),
                    "profile.jpg",
                    1L,
                    MediaType.IMAGE_JPEG_VALUE
            );
            CreateUserDto createUserDto = CreateUserDtoFixture.createUserDto();
            UUID userId = UUID.randomUUID();
            UserResponseDto userResponseDto = UserResponseDtoFixture.createUserResponseWithProfile(userId, createUserDto.email(), createUserDto.username(), binaryContentResponseDto);

            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createUserDto)
            );

            MockMultipartFile profile = new MockMultipartFile(
                    "profile",
                    "profile.jpg",
                    MediaType.IMAGE_PNG_VALUE,
                    "profile".getBytes()
            );

            given(userService.createUser(any(CreateUserDto.class), argThat(Optional::isPresent)))
                    .willReturn(userResponseDto);
            // when & then
            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .file(profile)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(userId.toString()))
                    .andExpect(jsonPath("$.username").value("test"))
                    .andExpect(jsonPath("$.profile.fileName").value("profile.jpg"));

            then(userService).should().createUser(any(CreateUserDto.class), argThat(Optional::isPresent));
        }

        @Test
        @DisplayName("[예외 케이스] - 유저 생성 실패 (request 필드 누락)")
        void createUser_fail() throws Exception {

            CreateUserDto createUserDto = new CreateUserDto(
                    "", // username이 빈칸인 경우
                    "test_123",
                    "test@codeit.com"
            );

            // given
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createUserDto)
            );

            // when & then
            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
            then(userService).should(never()).createUser(any(CreateUserDto.class), any(Optional.class));
        }
    }

    @Nested
    @DisplayName("전체 유저 조회 테스트")
    class GetAllUser {
        private UserResponseDto userResponseDto1;
        private UserResponseDto userResponseDto2;
        private UserResponseDto userResponseDto3;

        @BeforeEach
        void setup() {
            UUID userId1 = UUID.randomUUID();
            UUID userId2 = UUID.randomUUID();
            UUID userId3 = UUID.randomUUID();

            userResponseDto1 = UserResponseDtoFixture.createUserResponse(userId1, "test1@codeit.com", "tester1");
            userResponseDto2 = UserResponseDtoFixture.createUserResponse(userId2, "test2@codeit.com", "tester2");
            userResponseDto3 = UserResponseDtoFixture.createUserResponse(userId3, "test3@codeit.com", "tester3");
        }

        @Test
        @DisplayName("[정상 케이스] - 유저 전체 조회 성공")
        void getAllUsers_success() throws Exception {

            // given
            given(userService.getAllUsers())
                    .willReturn(List.of(userResponseDto1, userResponseDto2, userResponseDto3));

            // when & then
            mockMvc.perform(get("/api/users")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)));
        }

        @Test
        @DisplayName("[정상 케이스] - 유저가 없는 경우")
        void getAllUsers_nonUser_success() throws Exception {

            // given
            given(userService.getAllUsers())
                    .willReturn(List.of());

            // when & then
            mockMvc.perform(get("/api/users")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));

        }
    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    class UserDelete {

        @Test
        @DisplayName("[정상 케이스] - 유저 삭제 성공")
        void deleteUser_success() throws Exception {
            // given
            UUID userId = UUID.randomUUID();
            willDoNothing().given(userService).deleteUser(any());

            // when & then
            mockMvc.perform(delete("/api/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            then(userService).should().deleteUser(userId);
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저 ")
        void deleteUser_notFound_fail() throws Exception {
            // given
            UUID notFoundId = UUID.randomUUID();

            //void 메서드의 예외 발생을 설정할 때는
            // doThrow() 또는 willThrow()를 맨 앞에
            doThrow(UserNotFoundException.byId(notFoundId))
                    .when(userService).deleteUser(notFoundId);


            // when & then
            mockMvc.perform(delete("/api/users/{userId}", notFoundId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            then(userService).should().deleteUser(notFoundId);

        }
    }

    @Nested
    @DisplayName("유저 수정 테스트")
    class UserUpdate {
        private UpdateUserDto updateUserDto;
        private UserResponseDto updatedUserResponseDto;
        private UUID userId;

        @BeforeEach
        void setup() {
            updateUserDto = new UpdateUserDto(
                    "new_test",
                    "new_test@codeit.com",
                    "new_test_123"
            );
            userId = UUID.randomUUID();
            updatedUserResponseDto = UserResponseDtoFixture.createUserResponse(userId, updateUserDto.newEmail(), updateUserDto.newUsername());
        }

        @Test
        @DisplayName("[정상 케이스] - 유저 정보 수정 성공")
        void userUpdate_success() throws Exception {
            // given
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userUpdateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(updateUserDto)
            );

            given(userService.updateUser(eq(userId), any(UpdateUserDto.class), any(Optional.class)))
                    .willReturn(updatedUserResponseDto);

            // when & then
            mockMvc.perform(multipart("/api/users/{userId}", userId)
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    }
                            ))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(userId.toString()))
                    .andExpect(jsonPath("$.username").value(updatedUserResponseDto.username()));

            then(userService).should().updateUser(eq(userId), any(UpdateUserDto.class), any(Optional.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저")
        void userUpdate_notFound_fail() throws Exception {
            // given
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userUpdateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(updateUserDto)
            );

            UUID notFoundId = UUID.randomUUID();

            doThrow(UserNotFoundException.byId(notFoundId))
                    .when(userService).updateUser(eq(notFoundId), any(UpdateUserDto.class), any(Optional.class));

            // when & then
            mockMvc.perform(multipart("/api/users/{userId}", notFoundId)
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(request -> {
                                        request.setMethod("PATCH");
                                        return request;
                                    }
                            ))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            then(userService).should().updateUser(eq(notFoundId), any(UpdateUserDto.class), argThat(Optional::isEmpty));
        }
    }
}