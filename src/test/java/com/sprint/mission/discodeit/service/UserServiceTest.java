package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.UserServiceImpl;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("UserService Test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {

        @Test
        @DisplayName("성공: 회원가입 성공")
        void createUser_Success() {
            // given
            UserCreateRequest request = new UserCreateRequest("test", "test@codeit.com", "Password1!");

            when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(request.username())).thenReturn(Optional.empty());

            User testUser = User.builder()
                    .username("test")
                    .email("test@codeit.com")
                    .build();

            when(userRepository.save(any(User.class)))
                    .thenReturn(testUser);

            UserDto result = UserDto.builder()
                    .username("test")
                    .email("test@codeit.com")
                    .build();
            when(userMapper.toDto(any(User.class))).thenReturn(result);

            // when
            UserDto user = userService.createUser(request, null);

            // then
            assertThat(user).isNotNull();
            assertThat(user.email()).isEqualTo("test@codeit.com");
            assertThat(user.username()).isEqualTo("test");
            verify(userRepository, times(1)).save(any(User.class));
            verify(userMapper, times(1)).toDto(any(User.class));
            verify(userRepository, times(1)).findByEmail(request.email());
            verify(userRepository, times(1)).findByUsername(request.username());
            verify(binaryContentRepository, never()).save(any());
            verify(binaryContentStorage, never()).put(any(), any());
        }

        @Test
        @DisplayName("실패: 중복된 이메일")
        void createUser_DuplicateEmail_ThrowException() {
            // given
            UserCreateRequest request
                    = new UserCreateRequest("test", "test@codeit.com", "Password1!");

            User existUser = User.builder().email("test").build();

            when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(existUser));

            // when & then
            assertThatThrownBy(() -> userService.createUser(request, null))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("이미 존재하는 이메일입니다.");
            verify(userRepository,never()).save(any());
        }

        @Test
        @DisplayName("실패: 중복된 이름")
        void createUser_DuplicateName_ThrowException() {
            // given
            UserCreateRequest request
                    = new UserCreateRequest("test", "test@codeit.com", "Password1!");

            User existUser = User.builder().username("test").build();

            when(userRepository.findByUsername(request.username())).thenReturn(Optional.of(existUser));

            // when & then
            assertThatThrownBy(() -> userService.createUser(request, null))
                    .isInstanceOf(DuplicateNameException.class)
                    .hasMessage("이미 존재하는 이름입니다.");
            verify(userRepository,never()).save(any());
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {

        @Test
        @DisplayName("성공: 유저 정보를 수정할 수 있다.")
        void updateUser_Success() {
            // given
            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .username("oldName")
                    .email("old@email.com")
                    .password("OldPassword1!")
                    .build();
            ReflectionTestUtils.setField(user, "id", userId);

            UserUpdateRequest update
                    = new UserUpdateRequest("newName", "new@email.com", null);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByEmail(update.newEmail())).thenReturn(Optional.empty());
            when(userRepository.findByUsername(update.newUsername())).thenReturn(Optional.empty());


            UserDto result = UserDto.builder()
                    .username("newName")
                    .email("new@email.com")
                    .build();
            when(userMapper.toDto(any(User.class))).thenReturn(result);

            // when
            userService.updateUserInfo(userId, update, null);

            // then
            assertThat(result.email()).isEqualTo("new@email.com");
            assertThat(result.username()).isEqualTo("newName");
            verify(userRepository, times(1)).findByEmail("new@email.com");
            verify(userRepository, times(1)).findByUsername("newName");
            verify(userMapper, times(1)).toDto(any(User.class));
            verify(userRepository, times(1)).save(any(User.class));
        }
        
        @Test
        @DisplayName("업데이트 실패: 이메일 중복")
        void updateUser_DuplicateEmail_ThrowException() {
            // given
            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .username("oldName")
                    .email("old@email.com")
                    .password("OldPassword1!")
                    .build();
            ReflectionTestUtils.setField(user, "id", userId);

            User existUser = User.builder().email("new@email.com").build();
            ReflectionTestUtils.setField(existUser, "id", UUID.randomUUID());

            UserUpdateRequest update = new UserUpdateRequest(null, "new@email.com", null);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByEmail(update.newEmail())).thenReturn(Optional.of(existUser));

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(userId, update, null))
                    .isInstanceOf(DuplicateEmailException.class)
                    .hasMessage("이미 존재하는 이메일입니다.");
            verify(userRepository,never()).save(any());
        }

        @Test
        @DisplayName("업데이트 실패: 이름 중복")
        void updateUser_DuplicateName_ThrowException() {
            // given
            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .username("oldName")
                    .email("old@email.com")
                    .password("OldPassword1!")
                    .build();
            ReflectionTestUtils.setField(user, "id", userId);

            User existUser = User.builder().username("newName").build();
            ReflectionTestUtils.setField(existUser, "id", UUID.randomUUID());

            UserUpdateRequest update = new UserUpdateRequest("newName", null, null);

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userRepository.findByUsername(update.newUsername())).thenReturn(Optional.of(existUser));

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(userId, update, null))
                    .isInstanceOf(DuplicateNameException.class)
                    .hasMessage("이미 존재하는 이름입니다.");
            verify(userRepository,never()).save(any());
        }
    }

    @Nested
    @DisplayName("유저를 삭제할 수 있다.")
    class DeleteUser {

        @Test
        @DisplayName("유저 삭제 성공")
        void deleteUser_Success() {
            // given
            UUID userId = UUID.randomUUID();

            User user = User.builder()
                    .build();
            ReflectionTestUtils.setField(user, "id", userId);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            // when
            userService.deleteUser(userId);

            // then
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("유저 삭제 실패: 찾을 수 없음")
        void deleteUser_NotFound_ThrowException() {
            // given
            UUID userId = UUID.randomUUID();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.deleteUser(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("사용자를 찾을 수 없습니다.");
            verify(userRepository, times(1)).findById(userId);
            verify(userRepository, never()).deleteById(userId);
            verify(userRepository, never()).save(any());
        }
    }
}