package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.user.*;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserDuplicateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private BinaryContentRepository binaryContentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
    }


    @Nested
    @DisplayName("signUp")
    class SignUp {
        @Test
        @DisplayName("[Integration][Flow][Positive] 회원가입 - 저장 후 조회 시 동일 데이터 반환")
        void signUp_then_persists_and_returns_same_data() {
            // given
            int before = userRepository.findAll().size();

            //when
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("name", "example@email.com", "password"), null);
            UserResponseDto userResponseDto = userService.signUp(command);

            //then
            int after = userRepository.findAll().size();
            assertEquals(before + 1, after);
            User persistedUser = userRepository.findById(userResponseDto.id())
                    .orElseThrow(() -> new AssertionError("User not found"));
            assertEquals("name", persistedUser.getUsername());

            assertEquals("example@email.com", persistedUser.getEmail());
            assertNotEquals("password", persistedUser.getPassword());
            assertTrue(passwordEncoder.matches("password", persistedUser.getPassword()));
        }


        @Test
        @DisplayName("[Integration][Negative] 회원가입 - 중복된 이메일일 경우 예외")
        void signup_whenDuplicate_email_thenThrows() {
            // given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("name", "example@email.com", "password"), null);
            userService.signUp(command);
            int before = userRepository.findAll().size();

            // when & then
            UserSignupCommand duplicateCommand = UserSignupCommand.from(new UserSignupRequestDto("different", "example@email.com", "password"), null);
            assertThrows(UserDuplicateException.class, () -> userService.signUp(duplicateCommand));

            // then
            int after = userRepository.findAll().size();
            assertEquals(before, after);
        }

        @Test
        @DisplayName("[Integration][Negative] 회원가입 - 중복된 이름일 경우 예외")
        void signup_whenDuplicate_nickname_thenThrows() {
            // given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("name", "example@email.com", "password"), null);
            userService.signUp(command);
            int before = userRepository.findAll().size();

            // when & then
            UserSignupCommand duplicateCommand = UserSignupCommand.from(new UserSignupRequestDto("name", "different@email.com", "password"), null);
            assertThrows(UserDuplicateException.class, () -> userService.signUp(duplicateCommand));

            // then
            int after = userRepository.findAll().size();
            assertEquals(before, after);
        }

        @Test
        @DisplayName("[Integraton][Flow][Positive] 회원가입 - UserStatus도 함께 생성된다.")
        void signup_then_userStatus_persists_as_well() {

            // given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("name",
                    "example@email.com",
                    "password"), null);
            UserResponseDto responseDto = userService.signUp(command);

            // when
            UserStatus userStatusbByUserId = userStatusRepository
                    .findByUserId(responseDto.id()).orElseThrow(() -> new NoSuchElementException("회원정보없음"));

            // then
            assertNotNull(userStatusbByUserId);
            assertEquals(responseDto.id(), userStatusbByUserId.getUser().getId());

        }

    }

    @Nested
    @DisplayName("getUserById")
    class GetUserById {

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원조회 - 저장후 response DTO 반환 조회 성공")
        void getUserById_returns_saved_user() {
            //given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("name", "example@email.com", "password"), null);
            UserResponseDto responseDto = userService.signUp(command);

            //when
            UserResponseDto userById = userService.getUserById(responseDto.id());

            //then
            assertEquals("name", userById.username());
            assertEquals(responseDto.id(), userById.id());
        }

        @Test
        @DisplayName("[Integration][Exception] 회원조회 - 미존재 → 예외 전파")
        void getUserById_throws_when_not_found() {
            assertThrows(UserNotFoundException.class,
                    () -> userService.getUserById(UUID.randomUUID()));
        }

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원조회 - 회원 상태 정보도 가져온다.")
        void getUserById_returns_user_status() {

        }


    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("[Integration][Flow][Positive] 회원수정 - 이메일만 변경, 나머지 유지")
        void updateUser_updates_only_email() {
            //given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", "pw"), null);
            UserResponseDto responseDto = userService.signUp(command);
            Instant beforeTime = userRepository.findById(responseDto.id()).orElseThrow().getUpdatedAt();
            UserUpdateCommand updateCommand = UserUpdateCommand.from(responseDto.id(), new UserUpdateRequestDto(null, "b@c.com", null), null);
            //when
            userService.updateUser(updateCommand);

            em.flush();
            em.clear();

            //then
            User after = userRepository.findById(responseDto.id()).orElseThrow();
            assertEquals("b@c.com", after.getEmail());
            assertEquals("nick", after.getUsername());

            // 실제 update호출안된건지 update값 조회
            assertTrue(after.getUpdatedAt().isAfter(beforeTime));
        }

        @Test
        @DisplayName("[Integration][Negative] 회원수정 - 변경 없음 → 상태 변화 없음")
        void updateUser_no_effect_when_same_values() {

            //given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", "pw"), null);
            UserResponseDto responseDto = userService.signUp(command);
            User before = userRepository.findById(responseDto.id()).orElseThrow();
            Instant beforeTime = before.getUpdatedAt(); // 스냅샷
            UserUpdateCommand updateCommand = UserUpdateCommand.from(responseDto.id(), new UserUpdateRequestDto("nick", "a@b.com", "pw"), null);
            UserUpdateCommand updateCommand2 = UserUpdateCommand.from(responseDto.id(), new UserUpdateRequestDto(null, null, null), null);


            //when
            userService.updateUser(updateCommand);
            userService.updateUser(updateCommand2);

            //then
            User after = userRepository.findById(responseDto.id()).orElseThrow();
            assertEquals(before.getUsername(), after.getUsername());
            assertEquals(before.getEmail(), after.getEmail());

            assertEquals(beforeTime, after.getUpdatedAt());
        }

        @Test
        @DisplayName("[Integration][State] 회원수정 - 여러 필드 동시 변경 반영")
        void updateUser_updates_multiple_fields() {
            // given
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", "pw"), null);
            UserResponseDto responseDto = userService.signUp(command);
            Instant beforeTime = userRepository.findById(responseDto.id()).orElseThrow().getUpdatedAt();
            MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());
            UserUpdateCommand updateCommand = UserUpdateCommand.from(responseDto.id(), new UserUpdateRequestDto("nick2", "b@c.com", "pw2"), mockMultipartFile);

            // when
            userService.updateUser(updateCommand);

            em.flush();
            em.clear();

            User after = userRepository.findById(responseDto.id()).orElseThrow();
            assertEquals("nick2", after.getUsername());
            assertEquals("b@c.com", after.getEmail());
            assertNotNull(after.getProfile().getId());

            assertTrue(after.getUpdatedAt().isAfter(beforeTime));
        }
    }

    @Nested
    @DisplayName("deleteUser")
    class DeleteUser {

        @Test
        @DisplayName("[Integration][Flow] 회원삭제 - 삭제 후 조회 시 예외")
        void deleteUser_then_not_found() {
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", "pw"), null);
            UserResponseDto responseDto = userService.signUp(command);
            userService.deleteUser(responseDto.id());
            assertThrows(UserNotFoundException.class, () -> userService.getUserById(responseDto.id()));
        }

        @Test
        @DisplayName("[Integration][Flow] 회원삭제 - 회원삭제시 해당 연관 프로필, 유저상태 데이터 삭제")
        void deleteUser_then_deletes_profile_and_status() {
            // given
            // 결정적 픽스쳐 준비
            byte[] payload = "fake-bytes".getBytes(UTF_8);
            // 프로필이미지
            BinaryContent savedBinarycontent = binaryContentRepository.save(
                    new BinaryContent("profile.png", "image/png", (long) payload.length)
            );
            User user = User.create("nick", "a@b.com", "pw", savedBinarycontent);
            user.initUserStatus();
            //유저등록
            User savedUser = userRepository.save(user);
            // 유저상태
//            UserStatus savedUserStatus = userStatusRepository.save(userStatus);

            // preconditions
            assertAll(
                    () -> assertTrue(userRepository.findById(savedUser.getId()).isPresent()),
                    () -> assertTrue(userStatusRepository.findByUserId(savedUser.getId()).isPresent()),
                    () -> assertTrue(binaryContentRepository.findById(savedBinarycontent.getId()).isPresent())
            );

            // when
            userService.deleteUser(savedUser.getId());

            // then
            assertAll(
                    () -> assertTrue(userRepository.findById(savedUser.getId()).isEmpty()),
                    () -> assertTrue(userStatusRepository.findByUserId(savedUser.getId()).isEmpty()),
                    () -> assertTrue(binaryContentRepository.findById(savedBinarycontent.getId()).isEmpty())
            );
        }
    }

    @Nested
    @DisplayName("getAllUsers")
    class GetAllUsers {
        @Test
        @DisplayName("[Integration][Flow] 회원전체조회 - 여러 명 저장 후 전체 조회")
        void getAllUsers_returns_all() {
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", "pw"), null);
            UserSignupCommand command2 = UserSignupCommand.from(new UserSignupRequestDto("b", "b@b.com", "p"), null);
            userService.signUp(command);
            userService.signUp(command2);
            assertTrue(userService.getAllUsers().size() >= 2);
        }
    }

    @Nested
    @DisplayName("getUsersByIds")
    class GetUsersByIds {
        @Test
        @DisplayName("[Integration][Flow] 특정 회원리스트 조회 - 일부 id만 유효 → 유효한 것만 반환")
        void getUsersByIds_returns_only_existing() {
            UserSignupCommand command = UserSignupCommand.from(new UserSignupRequestDto("nick", "a@b.com", "pw"), null);
            UserResponseDto responseDto = userService.signUp(command);
            UUID id2 = UUID.randomUUID();
            List<User> result = userService.getUsersByIds(List.of(responseDto.id(), id2));
            assertEquals(1, result.size());
            assertEquals(responseDto.id(), result.get(0).getId());
        }

        @Test
        @DisplayName("[Integration][Boundary] 빈 리스트 → 빈 반환")
        void getUsersByIds_empty_ids_returns_empty() {
            assertTrue(userService.getUsersByIds(List.of()).isEmpty());
        }

        @Test
        @DisplayName("[Integration][Negative] null 입력 → 예외")
        void getUsersByIds_null_ids_throws() {
            assertThrows(DiscodeitException.class, () -> userService.getUsersByIds(null));
        }
    }

}


