package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.userDto.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.dto.userDto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.Role;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.FileOperationFailedException;
import com.sprint.mission.discodeit.exception.user.DuplicateEmailException;
import com.sprint.mission.discodeit.exception.user.DuplicateNameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.fixture.UserFixture;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.UserServiceImpl;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Test")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AuthService authService;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("유저 생성")
    class CreateUser {

        static Stream<MultipartFile> emptyProfile() {
            return Stream.of(
                    null,
                    new MockMultipartFile("profile", new byte[0])
            );
        }

        @ParameterizedTest
        @MethodSource("emptyProfile")
        @DisplayName("""
                유저를 생성할 수 있다.
                프로필 이미지는 비어있거나 null이다.
                """)
        void userCreate_Success(MultipartFile multipartFile) {
            // given
            UserCreateRequest request = UserFixture.getUserRequest(1);
            UserDto dto = UserFixture.getUserDto(1);

            when(passwordEncoder.encode(any())).thenReturn(UserFixture.PASSWORD);
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);

            // when
            UserDto response = userService.createUser(request, multipartFile);

            // then
            verify(userRepository, times(1)).save(any());
            verify(binaryContentRepository, never()).save(any());
            assertThat(response).isEqualTo(dto);
        }

        @Test
        @DisplayName("프로필 이미지가 있는 유저를 생성할 수 있다.")
        void userCreateWithProfileImagine_Success() {
            // given
            UserCreateRequest request = UserFixture.getUserRequest(1);

            MultipartFile multipartFile = mock(MultipartFile.class);
            BinaryContent binaryContent = BinaryContent.builder().build();

            UserDto dto = UserFixture.getUserDto(1);

            when(binaryContentRepository.save(any())).thenReturn(binaryContent);
            when(passwordEncoder.encode(any())).thenReturn(UserFixture.PASSWORD);
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);

            // when
            UserDto response = userService.createUser(request, multipartFile);

            // then
            verify(userRepository, times(1)).save(any());
            verify(binaryContentRepository, times(1)).save(any());
            assertThat(response).isEqualTo(dto);
        }

        @Test
        @DisplayName("이메일이 중복되면 회원가입에 실패한다.")
        void userCreate_Fail_DuplicateEmail() {
            // given
            UserCreateRequest request = UserFixture.getUserRequest(1);
            when(userRepository.existsByEmail(any())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.createUser(request, null))
                    .isInstanceOf(DuplicateEmailException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.DUPLICATE_USER_EMAIL);
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("이름이 중복되면 회원가입에 실패한다.")
        void userCreate_Fail_DuplicateUsername() {
            // given
            UserCreateRequest request = UserFixture.getUserRequest(1);
            when(userRepository.existsByUsername(any())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.createUser(request, null))
                    .isInstanceOf(DuplicateNameException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.DUPLICATE_USER_NAME);
            verify(userRepository, never()).save(any());
        }
        
        @Test
        @DisplayName("프로필이미지를 읽는 중 오류가 발생하면 예외를 반환한다.")
        void binaryContentReadError_Fail() throws IOException {
            // given
            UserCreateRequest request = UserFixture.getUserRequest(1);

            MultipartFile multipartFile = mock(MultipartFile.class);
            BinaryContent binaryContent = BinaryContent.builder().build();
            ReflectionTestUtils.setField(binaryContent, "id", UUID.randomUUID());

            when(binaryContentRepository.save(any())).thenReturn(binaryContent);
            when(multipartFile.getBytes()).thenThrow(new IOException("예외 강제 발생"));

            // when & then
            assertThatThrownBy(() -> userService.createUser(request, multipartFile))
                    .isInstanceOf(FileOperationFailedException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FILE_OPERATION_FAILED);
        }
    }

    @Nested
    @DisplayName("유저 조회")
    class SearchUser {

        @Test
        @DisplayName("단일 유저를 조회할 수 있다.")
        void userSearch_Success() {
            // given
            User user = UserFixture.getUser(1);
            UserDto dto = UserFixture.getUserDto(1);

            when(userRepository.findById(any())).thenReturn(java.util.Optional.of(user));
            when(authService.isOnline(any())).thenReturn(false);
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);

            // when
            UserDto response = userService.findUserById(user.getId());

            // then
            verify(userMapper, times(1)).toDto(any(User.class), eq(false));
            assertThat(response).isEqualTo(dto);
        }

        @Test
        @DisplayName("유저를 전체 조회할 수 있다.")
        void userAllSearch_Success() {
            // given
            User user1 = UserFixture.getUser(1);
            User user2 = UserFixture.getUser(2);
            UserDto dto1 = UserFixture.getUserDto(1);
            UserDto dto2 = UserFixture.getUserDto(2);

            when(userRepository.findAll()).thenReturn(List.of(user1, user2));
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto1, dto2);
            // when
            List<UserDto> response = userService.findAllUsers();

            // then
            verify(userMapper, times(2)).toDto(any(User.class), eq(false));
            assertThat(response).containsExactlyInAnyOrder(dto1, dto2);
        }

        @Test
        @DisplayName("존재하지 않는 유저Id면 예외를 반환한다.")
        void userSearch_Fail_NotFound() {
            // given
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.findUserById(UUID.randomUUID()))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {

        private UserUpdateRequest request;

        @BeforeEach
        void setUp() {
            request = new UserUpdateRequest(
                    "newName", "new@a.a", "newPassword1!");
        }

        @Test
        @DisplayName("유저 정보를 수정할 수 있다.")
        void userUpdate_Success() {
            // given
            User user = UserFixture.getUser(1);
            UserDto dto = UserDto.builder().id(user.getId()).username("newName").email("new@a.a").build();

            MultipartFile multipartFile = mock(MultipartFile.class);
            BinaryContent binaryContent = BinaryContent.builder().build();

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);
            when(binaryContentRepository.save(any())).thenReturn(binaryContent);

            // when
            UserDto response = userService.updateUserInfo(user.getId(), request, multipartFile);

            // then
            assertThat(response).isEqualTo(dto);
        }

        @Test
        @DisplayName("프로필 이미지를 변경할 수 있다.")
        void userUpdateWithProfileImage_Success() {
            // given
            UserUpdateRequest emptyRequest = new UserUpdateRequest(null, null, null);
            User user = UserFixture.getUser(1);
            BinaryContent oldProfile = new BinaryContent();
            user.updateProfile(oldProfile);

            MultipartFile multipartFile = mock(MultipartFile.class);
            BinaryContent binaryContent = BinaryContent.builder()
                    .fileName("test").build();
            ReflectionTestUtils.setField(binaryContent, "id", UUID.randomUUID());

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(binaryContentRepository.save(any())).thenReturn(binaryContent);
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(UserFixture.getUserDto(1));

            // when
            userService.updateUserInfo(user.getId(), emptyRequest, multipartFile);

            // then
            verify(binaryContentRepository, times(1)).save(any());
            verify(userRepository, times(1)).save(any());
            verify(binaryContentRepository, times(1)).deleteById(eq(oldProfile.getId()));
        }

        @Test
        @DisplayName("존재하지 않는 유저Id면 예외를 반환한다.")
        void userUpdate_Fail_NotFound() {
            // given
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(UUID.randomUUID(), request, null))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }

        @Test
        @DisplayName("이름이 중복되면 변경되지 않는다.")
        void userUpdate_Fail_DuplicateName() {
            // given
            User user = UserFixture.getUser(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userRepository.existsByUsername(any())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(UUID.randomUUID(), request, null))
                    .isInstanceOf(DuplicateNameException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.DUPLICATE_USER_NAME);
        }

        @Test
        @DisplayName("이메일이 중복되면 변경되지 않는다.")
        void userUpdate_Fail_DuplicateEmail() {
            // given
            User user = UserFixture.getUser(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userRepository.existsByEmail(any())).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> userService.updateUserInfo(UUID.randomUUID(), request, null))
                    .isInstanceOf(DuplicateEmailException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.DUPLICATE_USER_EMAIL);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "null, null, null",
                "'', '', ''",
                "' ', ' ', ' '"
        }, nullValues = "null")

        @DisplayName("""
                새로운 이메일, 이름, 비밀번호가 비어있거나 null이라면 변경되지 않는다.
                프로필 이미지는 비어있다.
                """)
        void userUpdate_Fail_EmptyOrNull(String newEmail, String newName, String newPassword) {
            // given
            UserUpdateRequest failRequest = new UserUpdateRequest(newName, newEmail, newPassword);
            User user = UserFixture.getUser(1);
            UserDto dto = UserFixture.getUserDto(1);
            MultipartFile multipartFile = mock(MultipartFile.class);

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);
            when(multipartFile.isEmpty()).thenReturn(true);

            // when
            UserDto response = userService.updateUserInfo(user.getId(), failRequest, multipartFile);

            // then
            verify(userRepository, never()).save(any());
            verify(binaryContentRepository, never()).save(any());
            assertThat(response.email()).isEqualTo(user.getEmail());
            assertThat(response.username()).isEqualTo(user.getUsername());
        }

        @Test
        @DisplayName("원래 이메일, 이름, 비밀번호로는 변경할 수 없다.")
        void userUpdate_Fail_NotChange() {
            // given
            User user = UserFixture.getUser(1);
            UserUpdateRequest noChangeRequest
                    = new UserUpdateRequest(user.getUsername(), user.getEmail(), UserFixture.PASSWORD);
            UserDto dto = UserFixture.getUserDto(1);

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);
            when(passwordEncoder.matches(any(), any())).thenReturn(true);

            // when
            UserDto response = userService.updateUserInfo(user.getId(), noChangeRequest, null);

            // then
            verify(userRepository, never()).save(any());
            assertThat(response).isEqualTo(dto);
        }
    }

    @Nested
    @DisplayName("유저 권한 변경")
    class ChangeRole {

        @Test
        @DisplayName("유저 권한을 변경할 수 있다.")
        void userChangeRole_Success() {
            // given
            User user = UserFixture.getUser(1);
            RoleUpdateRequest request = new RoleUpdateRequest(user.getId(), Role.CHANNEL_MANAGER);

            UserDto dto
                    = new UserDto(user.getId(), user.getUsername(), user.getEmail(), null, false, Role.CHANNEL_MANAGER);

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(userMapper.toDto(any(User.class), eq(false))).thenReturn(dto);

            // when
            UserDto response = userService.updateUserRole(request);

            // then
            assertThat(response.role()).isEqualTo(Role.CHANNEL_MANAGER);
        }

        @Test
        @DisplayName("존재하지 않는 유저id면 예외를 반환한다.")
        void userChangeRole_Fail_NotFound() {
            // given
            RoleUpdateRequest request = new RoleUpdateRequest(UUID.randomUUID(), Role.CHANNEL_MANAGER);
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.updateUserRole(request))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("유저 삭제")
    class DeleteUser {

        @Test
        @DisplayName("유저를 삭제할 수 있따.")
        void userDelete_Success() {
            // given
            User user = UserFixture.getUser(1);
            when(userRepository.findById(any())).thenReturn(Optional.of(user));

            // when
            userService.deleteUser(user.getId());

            // then
            verify(userRepository, times(1)).findById(user.getId());
            verify(userRepository, times(1)).deleteById(user.getId());
        }

        @Test
        @DisplayName("존재하지 않는 유저Id면 예외를 반환한다.")
        void userDelete_Fail_NotFound() {
            // given
            UUID userId = UUID.randomUUID();
            when(userRepository.findById(any())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> userService.deleteUser(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);
            verify(userRepository, never()).deleteById(userId);
        }
    }
}