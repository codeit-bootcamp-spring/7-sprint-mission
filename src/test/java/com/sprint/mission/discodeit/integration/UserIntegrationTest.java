package com.sprint.mission.discodeit.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
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


    @Nested
    @DisplayName("유저 생성 통합 테스트")
    class CreateUser {
        private CreateUserDto createUserDto;

        @BeforeEach
        void setUp() {
            createUserDto = new CreateUserDto(
                    "test",
                    "test_123",
                    "test@codeit.com"
            );
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
                    "profile.jpg",
                    MediaType.IMAGE_PNG_VALUE,
                    "profile".getBytes()
            );

            // when
            MvcResult mvcResult = mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .file(profile)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
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
            assertThat(user.get().getProfile().getFileName()).isEqualTo("profile.jpg");

            Optional<BinaryContent> userProfile = binaryContentRepository.findById(user.get().getProfile().getId());
            assertThat(userProfile).isPresent();
            assertThat(userProfile.get().getFileName()).isEqualTo("profile.jpg");
        }

        @Test
        @DisplayName("[예외 케이스] - 유저 생성 실패 (중복된 username)")
        void createUser_duplicateUsername_fail() throws Exception {
            // given
            // 중복 확인을 위한 유저 생성
            User user = new User("test", "test111@codeit.com", "test_456", null);
            userRepository.save(user);

            // when
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createUserDto)
            );

            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            // then
            // DB에 저장되지 않고 기존 유저(test)만 있는지 확인
            assertThat(userRepository.findAll()).hasSize(1);
        }

        @Test
        @DisplayName("[예외 케이스] - 유저 생성 실패 (username 빈 문자열)")
        void createUser_fail() throws Exception {
            // given
            CreateUserDto createUserDto = new CreateUserDto("", "test_123", "test11@codeit.com");
            MockMultipartFile userRequest = new MockMultipartFile(
                    "userCreateRequest",
                    "",
                    MediaType.APPLICATION_JSON_VALUE,
                    objectMapper.writeValueAsBytes(createUserDto)
            );

            // when
            mockMvc.perform(multipart("/api/users")
                            .file(userRequest)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andDo(print())
                    .andExpect(status().isBadRequest());

            // then
            assertThat(userRepository.findAll()).isEmpty();

        }
    }

    @Nested
    @DisplayName("유저 삭제 통합 테스트")
    class UserDelete {
        @Test
        @DisplayName("[정상 케이스] - 유저 삭제 성공")
        void deleteUser_success() throws Exception {
            // given
            User user = new User("test", "test111@codeit.com", "test_456", null);
            userRepository.save(user);
            UUID userId = user.getId();
            // when
            mockMvc.perform(delete("/api/users/{userId}", userId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            // then
            assertThat(userRepository.findById(userId)).isEmpty();
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저 삭제")
        void deleteUser_notFoundUser_fail() throws Exception {
            // given
            User user = new User("test", "test111@codeit.com", "test_456", null);
            userRepository.save(user);
            UUID userId = user.getId();

            // when
            mockMvc.perform(delete("/api/users/{userId}", UUID.randomUUID())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());

            // then

            assertThat(userRepository.findById(userId)).isPresent();
        }
    }
}
