package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.fixture.user.dto.CreateUserDtoFixture;
import com.sprint.mission.discodeit.fixture.user.entity.UserEntityFixture;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("User 통합 테스트")
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    private User user;


    @Nested
    @DisplayName("유저 생성 통합 테스트")
    class CreateUser {
        private CreateUserDto createUserDto;

        @BeforeEach
        void setUp() {
            createUserDto = CreateUserDtoFixture.createUserDto("test");
            user = UserEntityFixture.createUser("test");
        }

        @Test
        @DisplayName("[정상 케이스] - 유저 생성")
        void createUser_success() throws Exception {
            // given
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createUserDto)
            );

            MockMultipartFile profile = new MockMultipartFile(
                    "profile",
                    "profile.png",
                    MediaType.IMAGE_PNG_VALUE,
                    "profile".getBytes()
            );

            // when
            MvcResult mvcResult = mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .file(profile)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.username").value("test"))
                    .andReturn();

            String responseBody = mvcResult.getResponse().getContentAsString();
            UserResponseDto userResponseDto = objectMapper.readValue(responseBody, UserResponseDto.class);

            // then
            // db에 저장되었는지 확인
            Optional<User> user = userRepository.findById(userResponseDto.id());
            assertThat(user).isPresent();
            assertThat(user.get().getUsername()).isEqualTo("test");
            assertThat(user.get().getProfile().getFileName()).isEqualTo("profile.png");

            Optional<BinaryContent> userProfile = binaryContentRepository.findById(user.get().getProfile().getId());
            assertThat(userProfile).isPresent();
            assertThat(userProfile.get().getFileName()).isEqualTo("profile.png");
        }

        @Test
        @DisplayName("[예외 케이스] - 유저 생성 실패 (중복된 username)")
        void createUser_duplicateUsername_fail() throws Exception {
            // given
            // 중복 확인을 위한 유저 저장
            userRepository.save(user);
            CreateUserDto duplicatedUserRequest = CreateUserDtoFixture.createUserDto("test");

            // when
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(duplicatedUserRequest)
            );

            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            // then
            // DB에 admin 및 test 2개
            assertThat(userRepository.findAll()).hasSize(2);
        }

        @Test
        @DisplayName("[예외 케이스] - 유저 생성 실패 (username 빈 문자열)")
        @Transactional
        void createUser_fail() throws Exception {
            // given
            CreateUserDto emptyNameUserDto = CreateUserDtoFixture.createUserDto("");
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(emptyNameUserDto)
            );

            // when
            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

        }
    }

    @Nested
    @DisplayName("유저 삭제 통합 테스트")
    class UserDelete {

        @Test
        @DisplayName("[정상 케이스] - 유저 삭제")
        void deleteUser_success() throws Exception {
            // given
            User user = UserEntityFixture.createUser("test");
            userRepository.save(user);
            UUID userId = user.getId();
            // when
            mockMvc.perform(delete("/api/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            // then
            assertThat(userRepository.findById(userId)).isEmpty();
        }

        @Test
        @DisplayName("[예외 케이스] - 자신이 아닌 유저 삭제 -> 권한 없음")
        void deleteUser_notFoundUser_fail() throws Exception {
            // given
            User user = UserEntityFixture.createUser("test");
            userRepository.save(user);
            UUID userId = user.getId();

            // when
            mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON)
                            .with(csrf()))
                    .andDo(print())
                    .andExpect(status().isForbidden());

            // then

            assertThat(userRepository.findById(userId)).isPresent();
        }
    }
}
