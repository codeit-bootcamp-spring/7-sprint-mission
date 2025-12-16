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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("User Service 테스트")
class BasicUserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    private CreateUserDto createUserDto;
    private UpdateUserDto updateUserDto;
    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        createUserDto = new CreateUserDto(
                "test",
                "test@codeit.com",
                "test_123"
        );

        user = new User(
                createUserDto.username(),
                createUserDto.email(),
                createUserDto.password(),
                null
        );

        UUID userId = UUID.randomUUID();
        ReflectionTestUtils.setField(user, "id", userId);

        userResponseDto = new UserResponseDto(
                userId,
                user.getUsername(),
                user.getEmail(),
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
            when(userRepository.findByUsername(createUserDto.username()))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail(createUserDto.email()))
                    .thenReturn(Optional.empty());
            when(userRepository.save(any(User.class)))
                    .thenReturn(user);
            when(userMapper.toResponseDto(any(User.class)))
                    .thenReturn(userResponseDto);

            // when
            UserResponseDto result = userService.createUser(createUserDto, Optional.empty());

            // then
            assertThat(result).isEqualTo(userResponseDto);
            assertThat(result.username()).isEqualTo(user.getUsername());

            verify(userRepository).findByUsername(createUserDto.username());
            verify(userRepository).findByEmail(createUserDto.email());
            verify(userRepository).save(any(User.class));
            verify(userMapper).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 username")
        void createUser_duplicateUsername_fail() {
            // given
            when(userRepository.findByUsername(createUserDto.username()))
                    .thenReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.createUser(createUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            verify(userRepository).findByUsername(createUserDto.username());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 email")
        void createUser_duplicateEmail_fail() {
            // given
            when(userRepository.findByEmail(createUserDto.email()))
                    .thenReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.createUser(createUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            verify(userRepository).findByEmail(createUserDto.email());
            verify(userRepository, never()).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("유저 조회 테스트")
    class UserRead {
        @Test
        @DisplayName("[정상 케이스] - 유저 조회")
        void readUser_success() {
            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));

            when(userMapper.toResponseDto(any(User.class)))
                    .thenReturn(userResponseDto);

            // when & then
            UserResponseDto result = userService.getUser(user.getId());

            assertThat(result).isEqualTo(userResponseDto);

            verify(userRepository).findById(user.getId());
            verify(userMapper).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[예외케이스] - 존재하지 않는 유저")
        void readUser_notFound_fail() {
            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.getUser(user.getId()))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository).findById(user.getId());
            verify(userMapper, never()).toResponseDto(any(User.class));
        }
    }

    @Nested
    @DisplayName("유저 업데이트 테스트")
    class UserUpdate {
        @Test
        @DisplayName("[정상 케이스] - 유저 업데이트")
        void updateUser_success() {

            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));
            when(userRepository.findByUsername(updateUserDto.newUsername()))
                    .thenReturn(Optional.empty());
            when(userRepository.findByEmail(updateUserDto.newEmail()))
                    .thenReturn(Optional.empty());

            UserResponseDto updatedResponseDto = new UserResponseDto(
                    user.getId(),
                    "updated_test",
                    "updated@codeit.com",
                    null,
                    true
            );

            when(userMapper.toResponseDto(any(User.class)))
                    .thenReturn(updatedResponseDto);

            // when
            UserResponseDto result = userService.updateUser(
                    user.getId(),
                    updateUserDto,
                    Optional.empty()
            );

            // then
            assertThat(result).isEqualTo(updatedResponseDto);
            assertThat(result.username()).isEqualTo("updated_test");
            assertThat(result.email()).isEqualTo("updated@codeit.com");

            // 실제로 값이 변경되었는가 확인
            assertThat(user.getUsername()).isEqualTo(updateUserDto.newUsername());
            assertThat(user.getEmail()).isEqualTo(updateUserDto.newEmail());

            verify(userRepository).findById(user.getId());
            verify(userRepository).findByUsername(updateUserDto.newUsername());
            verify(userRepository).findByEmail(updateUserDto.newEmail());
            verify(userMapper).toResponseDto(any(User.class));
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저")
        void updateUser_notFound_fail() {
            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() ->
                    userService.updateUser(user.getId(), updateUserDto, Optional.empty())
            ).isInstanceOf(UserNotFoundException.class);

            verify(userRepository).findById(user.getId());
            verify(userRepository, never()).save(any(User.class));
            verify(userMapper, never()).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 이름")
        void updateUser_duplicateUsername_fail() {
            // given
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));
            when(userRepository.findByUsername(updateUserDto.newUsername()))
                    .thenReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.updateUser(user.getId(), updateUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            verify(userRepository).findById(user.getId());
            verify(userRepository).findByUsername(updateUserDto.newUsername());
        }

        @Test
        @DisplayName("[예외 케이스] - 중복된 이메일")
        void updateUser_duplicateEmail_fail() {
            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));
            when(userRepository.findByEmail(updateUserDto.newEmail()))
                    .thenReturn(Optional.of(user));

            // when & then
            assertThatThrownBy(() ->
                    userService.updateUser(user.getId(), updateUserDto, Optional.empty())
            ).isInstanceOf(UserAlreadyExistsException.class);

            verify(userRepository).findById(user.getId());
            verify(userRepository).findByEmail(updateUserDto.newEmail());
        }
    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    class UserDelete {
        @Test
        @DisplayName("[정상 케이스] - 유저 삭제")
        void deleteUser_success() {
            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));
            doNothing().when(userRepository).delete(any(User.class));

            // when
            userService.deleteUser(user.getId());

            // then
            verify(userRepository).findById(user.getId());
            verify(userRepository).delete(user);
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저")
        void deleteUser_notFound_fail() {
            // given
            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() ->
                    userService.deleteUser(user.getId())
            ).isInstanceOf(UserNotFoundException.class);

            verify(userRepository).findById(user.getId());
            verify(userRepository, never()).delete(any(User.class));
        }
    }
}