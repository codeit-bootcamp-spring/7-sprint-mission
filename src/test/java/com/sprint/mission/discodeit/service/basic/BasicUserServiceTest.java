package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("User Service 테스트")
class BasicUserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private BasicUserService userService;

    private CreateUserDto createUserDto;
    private CreateUserDto createUserDto2;
    private CreateUserDto createUserDto3;
    private UpdateUserDto updateUserDto;
    private User user;
    private User user2;
    private User user3;
    private UserResponseDto userResponseDto;
    private UserResponseDto userResponseDto2;
    private UserResponseDto userResponseDto3;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto(
                "test",
                "test@codeit.com",
                "test_123"
        );
        createUserDto2 = new CreateUserDto(
                "test2",
                "test2@codeit.com",
                "test_456"
        );
        createUserDto3 = new CreateUserDto(
                "test3",
                "test3@codeit.com",
                "test_789"
        );


        user = new User(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                null
        );
        user2 = new User(
                createUserDto2.username(),
                createUserDto2.email(),
                createUserDto2.password(),
                null
        );
        user3 = new User(
                createUserDto3.username(),
                createUserDto3.email(),
                createUserDto3.password(),
                null
        );

        UUID userId = UUID.randomUUID();
        ReflectionTestUtils.setField(user, "id", userId);
        UUID userId2 = UUID.randomUUID();
        ReflectionTestUtils.setField(user2, "id", userId2);
        UUID userId3 = UUID.randomUUID();
        ReflectionTestUtils.setField(user3, "id", userId3);


        userResponseDto = new UserResponseDto(
                userId,
                user.getUsername(),
                user.getEmail(),
                null,
                true
        );
        userResponseDto2 = new UserResponseDto(
                userId2,
                user2.getUsername(),
                user2.getEmail(),
                null,
                true
        );
        userResponseDto3 = new UserResponseDto(
                userId3,
                user3.getUsername(),
                user3.getEmail(),
                null,
                true
        );


        updateUserDto = new UpdateUserDto(
                "updated_test",
                "updated@codeit.com",
                "updated_123"
        );
    }

    @Nested
    @DisplayName("유저 생성 테스트")
    class UserCreate {
        @Test
        @DisplayName("[정상 케이스] - 유저 생성")
        void createUser_success() {
            // given
            String rawPassword = createUserDto.password();
            String encodedPassword = "encoded_password"; // 가짜 암호화 결과값
            given(userRepository.findByUsername(createUserDto.username()))
                    .willReturn(Optional.empty());
            given(userRepository.findByEmail(createUserDto.email()))
                    .willReturn(Optional.empty());
            given(passwordEncoder.encode(rawPassword))
                    .willReturn(encodedPassword);
            given(userRepository.save(any(User.class)))
                    .willReturn(user);
            given(userMapper.toResponseDto(any(User.class)))
                    .willReturn(userResponseDto);

            // when
            UserResponseDto result = userService.createUser(createUserDto, Optional.empty());

            // then
            assertThat(result).isEqualTo(userResponseDto);
            assertThat(result.username()).isEqualTo(user.getUsername());

            then(userRepository).should().findByUsername(createUserDto.username());
            then(userRepository).should().findByEmail(createUserDto.email());
            then(userRepository).should().save(any(User.class));
            then(userMapper).should().toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 username")
        void createUser_duplicateUsername_fail() {
            // given
            given(userRepository.findByUsername(createUserDto.username()))
                    .willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.createUser(createUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            then(userRepository).should().findByUsername(createUserDto.username());
            then(userRepository).should(never()).save(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 email")
        void createUser_duplicateEmail_fail() {
            // given
            given(userRepository.findByEmail(createUserDto.email()))
                    .willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.createUser(createUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            then(userRepository).should().findByEmail(createUserDto.email());
            then(userRepository).should(never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("유저 조회 테스트")
    class UserRead {
        @Test
        @DisplayName("[정상 케이스] - 유저 조회")
        void readUser_success() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.of(user));

            given(userMapper.toResponseDto(any(User.class)))
                    .willReturn(userResponseDto);

            // when & then
            UserResponseDto result = userService.getUser(user.getId());

            assertThat(result).isEqualTo(userResponseDto);

            then(userRepository).should().findById(user.getId());
            then(userMapper).should().toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[예외케이스] - 존재하지 않는 유저")
        void readUser_notFound_fail() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.getUser(user.getId()))
                    .isInstanceOf(UserNotFoundException.class);

            then(userRepository).should().findById(user.getId());
            then(userMapper).should(never()).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[정상 케이스] - 모든 유저 조회 성공")
        void readUser_all_success() {
            // given
            given(userRepository.findAllWithProfileAndStatus())
                    .willReturn(List.of(user, user2, user3));
            given(userMapper.toResponseDto(user))
                    .willReturn(userResponseDto);
            given(userMapper.toResponseDto(user2))
                    .willReturn(userResponseDto2);
            given(userMapper.toResponseDto(user3))
                    .willReturn(userResponseDto3);
            // when
            List<UserResponseDto> result = userService.getAllUsers();

            // then
            assertThat(result).hasSize(3);
            assertThat(result).isEqualTo(List.of(userResponseDto, userResponseDto2, userResponseDto3));

            then(userRepository).should().findAllWithProfileAndStatus();
            then(userMapper).should(times(3)).toResponseDto(any(User.class));

        }
    }

    @Nested
    @DisplayName("유저 업데이트 테스트")
    class UserUpdate {
        @Test
        @DisplayName("[정상 케이스] - 유저 업데이트")
        void updateUser_success() {

            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.of(user));
            given(userRepository.findByUsername(updateUserDto.newUsername()))
                    .willReturn(Optional.empty());
            given(userRepository.findByEmail(updateUserDto.newEmail()))
                    .willReturn(Optional.empty());

            UserResponseDto updatedResponseDto = new UserResponseDto(
                    user.getId(),
                    "updated_test",
                    "updated@codeit.com",
                    null,
                    true
            );

            given(userMapper.toResponseDto(user))
                    .willReturn(updatedResponseDto);

            // when
            UserResponseDto result = userService.updateUser(
                    user.getId(),
                    updateUserDto,
                    Optional.empty()
            );

            // then
            assertThat(result).isEqualTo(updatedResponseDto);
            assertThat(result.username()).isEqualTo(updatedResponseDto.username());
            assertThat(result.email()).isEqualTo(updatedResponseDto.email());

            // 실제로 값이 변경되었는가 확인
            assertThat(user.getUsername()).isEqualTo(updateUserDto.newUsername());
            assertThat(user.getEmail()).isEqualTo(updateUserDto.newEmail());

            then(userRepository).should().findById(user.getId());
            then(userRepository).should().findByUsername(updateUserDto.newUsername());
            then(userRepository).should().findByEmail(updateUserDto.newEmail());
            then(userMapper).should().toResponseDto(any(User.class));
            then(userRepository).should(never()).save(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저")
        void updateUser_notFound_fail() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() ->
                    userService.updateUser(user.getId(), updateUserDto, Optional.empty())
            ).isInstanceOf(UserNotFoundException.class);

            then(userRepository).should().findById(user.getId());
            then(userRepository).should(never()).save(any(User.class));
            then(userMapper).should(never()).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 이름")
        void updateUser_duplicateUsername_fail() {
            // given
            given(userRepository.findById(user.getId()))
                    .willReturn(Optional.of(user));
            given(userRepository.findByUsername(updateUserDto.newUsername()))
                    .willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.updateUser(user.getId(), updateUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            then(userRepository).should().findById(user.getId());
            then(userRepository).should().findByUsername(updateUserDto.newUsername());
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 이메일")
        void updateUser_duplicateEmail_fail() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.of(user));
            given(userRepository.findByEmail(updateUserDto.newEmail()))
                    .willReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.updateUser(user.getId(), updateUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            then(userRepository).should().findById(user.getId());
            then(userRepository).should().findByEmail(updateUserDto.newEmail());
        }
    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    class UserDelete {
        @Test
        @DisplayName("[정상 케이스] - 유저 삭제")
        void deleteUser_success() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.of(user));
            doNothing().when(userRepository).delete(any(User.class));

            // when
            userService.deleteUser(user.getId());

            // then
            then(userRepository).should().findById(user.getId());
            then(userRepository).should().delete(user);
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저")
        void deleteUser_notFound_fail() {
            // given
            given(userRepository.findById(any(UUID.class)))
                    .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() ->
                    userService.deleteUser(user.getId())
            ).isInstanceOf(UserNotFoundException.class);

            then(userRepository).should().findById(user.getId());
            then(userRepository).should(never()).delete(any(User.class));
        }
    }
}