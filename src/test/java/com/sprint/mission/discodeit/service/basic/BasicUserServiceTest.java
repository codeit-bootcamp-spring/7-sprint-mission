package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("유저 서비스 테스트")
class BasicUserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private BinaryContentRepository binaryContentRepository;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private BasicUserService userService;

  private CreateUserRequestDto request;
  private User user;
  private UserResponseDto response;
  private UpdateUserDto updated;
  private UUID userId;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();

    request = new CreateUserRequestDto(
        "진우",
        "atto.jw@gmail.com",
        "1234567"
    );

    updated = new UpdateUserDto(
        "수정한 이름",
        "update@gmail.com",
        "7654321"
    );

    user = new User(
        "진우",
        "atto.jw@gmail.com",
        "1234567",
        null
    );

    response = new UserResponseDto(
        userId,
        "진우",
        "atto.jw@gmail.com",
        null,
        null
    );
  }

  @Nested
  @DisplayName("유저 생성")
  class CreateUser {

    @Test
    @DisplayName("유저를 생성할 수 있다")
    void createUser_Success() throws IOException {
      // given
      when(userRepository.findByUsername(request.username()))
          .thenReturn(Optional.empty()); // 중복이 아닌가?
      when(userRepository.findByEmail(request.email()))
          .thenReturn(Optional.empty());
      when(userRepository.save(any(User.class)))
          .thenReturn(user); //저장 성공 반환
      when(userMapper.toDto(any(User.class)))
          .thenReturn(response); // DTO 변환

      // when
      UserResponseDto response = userService.createUser(request, null);

      // then
      assertThat(response).isNotNull(); // null이 아닌가?
      assertThat(response.username()).isEqualTo("진우"); // 이름이 동일한가
      assertThat(response.email()).isEqualTo("atto.jw@gmail.com"); // 이메일이 동일한가

      // 제대로 호출되었는지
      verify(userRepository).findByUsername("진우");
      verify(userRepository).findByEmail("atto.jw@gmail.com");
      verify(userRepository).save(any(User.class));
      verify(userMapper).toDto(any(User.class));
    }

    @Test
    @DisplayName("중복된 이름으로 유저 생성시 예외 발생")
    void createUser_DuplicateUsername_ThrowsException() {
      // given
      when(userRepository.findByUsername(request.username()))
          .thenReturn(Optional.of(user));

      // when & then
      assertThatThrownBy(() -> userService.createUser(request, null))
          .isInstanceOf(DuplicateUsernameException.class);

      verify(userRepository).findByUsername("진우");
      verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("중복된 이메일로 유저 생성시 예외 발생")
    void createUser_DuplicateEmail_ThrowsException() {
      // given
      when(userRepository.findByUsername(request.username()))
          .thenReturn(Optional.empty());
      when(userRepository.findByEmail(request.email()))
          .thenReturn(Optional.of(user));

      // when & then
      assertThatThrownBy(() -> userService.createUser(request, null))
          .isInstanceOf(DuplicateEmailException.class);

      verify(userRepository).findByUsername("진우");
      verify(userRepository).findByEmail("atto.jw@gmail.com");
      verify(userRepository, never()).save(any(User.class));
    }
  }

  @Nested
  @DisplayName("유저 수정")
  class UpdateUser {

    @Test
    @DisplayName("유저를 수정 할 수 있다")
    void updateUser_Success() {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.of(user));
      when(userMapper.toDto(any(User.class)))
          .thenReturn(response);

      // when
      UserResponseDto updateUser = userService.updateUser(userId, updated, null);

      // then
      assertThat(updateUser).isNotNull();
      assertThat(updateUser).isEqualTo(response);

      verify(userRepository).findById(userId);
      verify(userMapper).toDto(any(User.class));
    }

    @Test
    @DisplayName("존재하지 않은 유저 수정 시 예외 발생")
    void updateUser_UserNotFound() {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> userService.updateUser(userId, updated, null))
          .isInstanceOf(UserNotFoundException.class);

      verify(userRepository).findById(userId);
      verify(userRepository, never()).save(any(User.class));
      verify(userMapper, never()).toDto(any(User.class));
    }

  }

  @Nested
  @DisplayName("유저 삭제")
  class deleteUser {

    @Test
    @DisplayName("유저를 삭제할 수 있다")
    void deleteUser_Success() {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.of(user));

      // when
      userService.deleteUser(userId);

      // then
      verify(userRepository).findById(userId);
      verify(userRepository).delete(user);
    }

    @Test
    @DisplayName("존재하지 않은 유저 삭제시 예외 발생")
    void deleteUser_UserNotFound() {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> userService.deleteUser(userId))
          .isInstanceOf(UserNotFoundException.class);

      verify(userRepository).findById(userId);
      verify(userRepository, never()).delete(user);

    }
  }
}