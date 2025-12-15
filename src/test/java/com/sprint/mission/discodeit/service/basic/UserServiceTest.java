package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@DisplayName("유저 서비스 단위 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicUserService userService;

    private MockMultipartFile mockFile() {
        return new MockMultipartFile(
                "file",
                "profile.png",
                "image/png",
                "dummy image".getBytes()
        );
    }

    @Nested
    @DisplayName("사용자 생성 테스트")
    class CreateUserTest {

        @Test
        @DisplayName("정상적으로 사용자를 생성할 수 있다")
        void createUser_Success() throws IOException {
            // given
            String email = "choonsik@kakao.com";
            String username = "kimchoonsik";
            String password = "choon1234!@";
            MockMultipartFile mockFile = mockFile();
            UUID binaryContentId = UUID.randomUUID();

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<UserStatus> userStatusCaptor = ArgumentCaptor.forClass(UserStatus.class);
            ArgumentCaptor<BinaryContent> binaryContentCaptor = ArgumentCaptor.forClass(BinaryContent.class);

            when(userRepository.existsByUsername(username))
                    .thenReturn(false);

            when(userRepository.existsByEmail(email))
                    .thenReturn(false);

            when(userRepository.save(any(User.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(userStatusRepository.save(any(UserStatus.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(binaryContentRepository.save(any(BinaryContent.class)))
                    .thenAnswer(i -> {
                        BinaryContent entity = i.getArgument(0);
                        ReflectionTestUtils.setField(entity, "id", binaryContentId);
                        return entity;
                    });

            when(binaryContentStorage.put(any(UUID.class), any(byte[].class)))
                    .thenReturn(binaryContentId);

            when(userMapper.toResponseDto(any(User.class)))
                    .thenAnswer(i -> {
                        User user = i.getArgument(0);
                        return new UserResponseDto(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                new BinaryContentResponseDto(
                                        null,
                                        mockFile.getOriginalFilename(),
                                        mockFile.getSize(),
                                        mockFile.getContentType(),
                                        mockFile.getBytes()
                                ),
                                user.getStatus().isOnline()
                        );
                    });

            // RequestDto 생성
            CreateUserRequestDto userRequestDto =
                    new CreateUserRequestDto(
                            email,
                            username,
                            password
            );

            CreateBinaryContentRequestDto binaryContentRequestDto =
                    new CreateBinaryContentRequestDto(
                            mockFile.getOriginalFilename(),
                            mockFile.getSize(),
                            mockFile.getContentType(),
                            mockFile.getBytes()
                    );

            // when
            UserResponseDto response = userService.create(userRequestDto, binaryContentRequestDto);

            // then
            assertThat(response).isNotNull();
            assertThat(response.email()).isEqualTo(email);
            assertThat(response.username()).isEqualTo(username);

            verify(userRepository, times(1)).save(userCaptor.capture());
            verify(userStatusRepository, times(1)).save(userStatusCaptor.capture());
            verify(binaryContentRepository, times(1)).save(binaryContentCaptor.capture());
            verify(binaryContentStorage, times(1)).put(eq(binaryContentId), eq(response.profile().bytes()));
            verify(userMapper, times(1)).toResponseDto(any(User.class));

            User captureUser = userCaptor.getValue();
            UserStatus captureUserStatus = userStatusCaptor.getValue();
            BinaryContent captureBinaryContent = binaryContentCaptor.getValue();

            assertThat(captureUser).isSameAs(captureUserStatus.getUser());
            assertThat(captureBinaryContent).isSameAs(captureUser.getProfile());
        }

        @Test
        @DisplayName("중복된 username인 경우 사용자 생성에 실패한다")
        void createUser_UsernameAlreadyExists() {
            // given
            String email = "choonsik@kakao.com";
            String username = "kimchoonsik";
            String password = "choon1234!@";

            // Repository가 save하면 저장된 내용을 그대로 반환
            when(userRepository.existsByUsername(username))
                    .thenReturn(true);

            // RequestDto 생성
            CreateUserRequestDto userRequestDto =
                    new CreateUserRequestDto(
                            email,
                            username,
                            password
                    );

            // when & then
            assertThatThrownBy(() -> userService.create(userRequestDto, null))
                    .isInstanceOf(UsernameAlreadyExistsException.class);

            verify(userRepository, never()).save(any(User.class));
            verify(userStatusRepository, never()).save(any(UserStatus.class));
            verify(binaryContentRepository, never()).save(any(BinaryContent.class));
            verify(binaryContentStorage, never()).put(any(UUID.class), any(byte[].class));
            verify(userMapper, never()).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("중복된 email인 경우 사용자 생성에 실패한다")
        void createUser_EmailAlreadyExists() {
            // given
            String email = "choonsik@kakao.com";
            String username = "kimchoonsik";
            String password = "choon1234!@";


            when(userRepository.existsByUsername(username))
                    .thenReturn(false);

            when(userRepository.existsByEmail(email))
                    .thenReturn(true);

            // RequestDto 생성
            CreateUserRequestDto userRequestDto =
                    new CreateUserRequestDto(
                            email,
                            username,
                            password
                    );

            // when & then
            assertThatThrownBy(() -> userService.create(userRequestDto, null))
                    .isInstanceOf(EmailAlreadyExistsException.class);

            verify(userRepository, never()).save(any(User.class));
            verify(userStatusRepository, never()).save(any(UserStatus.class));
            verify(binaryContentRepository, never()).save(any(BinaryContent.class));
            verify(binaryContentStorage, never()).put(any(UUID.class), any(byte[].class));
            verify(userMapper, never()).toResponseDto(any(User.class));
        }
    }

    @Nested
    @DisplayName("사용자 수정 테스트")
    class UpdateUserTest {

        // 사용자 정보만 전달한 경우, 프로필 이미지만 전달한 경우도 추가 가능
        // 기존 사용자의 정보를 동일하게 적은 경우에는 예외 발생 x

        @Test
        @DisplayName("정상적으로 사용자의 정보와 프로필 이미지를 수정할 수 있다")
        void updateUser_Success() throws IOException {
            // given
            UUID userId = UUID.randomUUID();
            UUID beforeProfileId = UUID.randomUUID();
            UUID afterProfileId = UUID.randomUUID();

            String newUsername = "test2";
            String newEmail = "test2@naver.com";
            String newPassword = "test12345";
            MockMultipartFile mockFile = mockFile();

            // 기존 사용자 정보
            BinaryContent profile = new BinaryContent(
                    mockFile.getOriginalFilename(),
                    mockFile.getSize(),
                    mockFile.getContentType()
            );
            ReflectionTestUtils.setField(profile, "id", beforeProfileId);

            User beforeUser = new User(
                    "test1",
                    "test1@naver.com",
                    "test1234",
                    profile
            );
            ReflectionTestUtils.setField(beforeUser, "id", userId);
            ReflectionTestUtils.setField(beforeUser, "status", new UserStatus(beforeUser));

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            ArgumentCaptor<BinaryContent> binaryContentCaptor = ArgumentCaptor.forClass(BinaryContent.class);

            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(beforeUser));

            when(userRepository.existsByUsername(newUsername))
                    .thenReturn(false);

            when(userRepository.existsByEmail(newEmail))
                    .thenReturn(false);

            when(binaryContentRepository.save(any(BinaryContent.class)))
                    .thenAnswer(i -> {
                        BinaryContent entity = i.getArgument(0);
                        ReflectionTestUtils.setField(entity, "id", afterProfileId);
                        return entity;
                    });

            when(binaryContentStorage.put(any(UUID.class), any(byte[].class)))
                    .thenReturn(afterProfileId);

            when(userRepository.save(any(User.class)))
                    .thenAnswer(i -> i.getArgument(0));

            when(userMapper.toResponseDto(any(User.class)))
                    .thenAnswer(i -> {
                        User user = i.getArgument(0);
                        return new UserResponseDto(
                                user.getId(),
                                user.getUsername(),
                                user.getEmail(),
                                null,
                                user.getStatus().isOnline()
                        );
                    });

            // RequestDto 생성
            UpdateUserRequestDto userRequest = new UpdateUserRequestDto(
                    newEmail,
                    newUsername,
                    newPassword
            );

            CreateBinaryContentRequestDto profileRequest =
                    new CreateBinaryContentRequestDto(
                            mockFile.getOriginalFilename(),
                            mockFile.getSize(),
                            mockFile.getContentType(),
                            mockFile.getBytes()
                    );


            // when
            UserResponseDto response = userService.update(userId, userRequest, profileRequest);

            // then
            assertThat(response).isNotNull();
            assertThat(response.username()).isEqualTo(newUsername);
            assertThat(response.email()).isEqualTo(newEmail);

            verify(userRepository, times(1)).findById(userId);
            verify(binaryContentRepository, times(1)).deleteById(beforeProfileId);
            verify(binaryContentRepository, times(1)).save(binaryContentCaptor.capture());
            verify(binaryContentStorage, times(1)).put(eq(afterProfileId), any(byte[].class));
            verify(userRepository, times(1)).save(userCaptor.capture());
            verify(userMapper, times(1)).toResponseDto(any(User.class));

            User captureUser = userCaptor.getValue();

            BinaryContent captureBinaryContent = binaryContentCaptor.getValue();
            assertThat(captureBinaryContent).isSameAs(captureUser.getProfile());
        }

        @Test
        @DisplayName("중복된 username인 경우 사용자 수정에 실패한다")
        void updateUser_UsernameAlreadyExists() throws IOException {
            // given
            UUID userId = UUID.randomUUID();

            String newUsername = "test2";
            String newEmail = "test2@naver.com";
            String newPassword = "test12345";
            MockMultipartFile mockFile = mockFile();

            // 기존 사용자 정보
            User beforeUser = new User(
                    "test1",
                    "test1@naver.com",
                    "test1234",
                    null
            );

            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(beforeUser));

            when(userRepository.existsByUsername(newUsername))
                    .thenReturn(true);

            // RequestDto 생성
            UpdateUserRequestDto userRequest = new UpdateUserRequestDto(
                    newEmail,
                    newUsername,
                    newPassword
            );

            CreateBinaryContentRequestDto profileRequest =
                    new CreateBinaryContentRequestDto(
                            mockFile.getOriginalFilename(),
                            mockFile.getSize(),
                            mockFile.getContentType(),
                            mockFile.getBytes()
                    );

            // when & then
            assertThatThrownBy(() -> userService.update(userId, userRequest, profileRequest))
                    .isInstanceOf(UsernameAlreadyExistsException.class);

            verify(binaryContentRepository, never()).deleteById(any(UUID.class));
            verify(binaryContentRepository, never()).save(any(BinaryContent.class));
            verify(binaryContentStorage, never()).put(any(UUID.class), any(byte[].class));
            verify(userRepository, never()).save(any(User.class));
            verify(userMapper, never()).toResponseDto(any(User.class));
        }

        @Test
        @DisplayName("중복된 email인 경우 사용자 수정에 실패한다")
        void updateUser_EmailAlreadyExists() throws IOException {
            // given
            UUID userId = UUID.randomUUID();

            String newUsername = "test2";
            String newEmail = "test2@naver.com";
            String newPassword = "test12345";
            MockMultipartFile mockFile = mockFile();

            // 기존 사용자 정보
            User beforeUser = new User(
                    "test3",
                    "test3@naver.com",
                    "test1234",
                    null
            );

            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(beforeUser));

            when(userRepository.existsByUsername(newUsername))
                    .thenReturn(false);

            when(userRepository.existsByEmail(newEmail))
                    .thenReturn(true);

            // RequestDto 생성
            UpdateUserRequestDto userRequest = new UpdateUserRequestDto(
                    newEmail,
                    newUsername,
                    newPassword
            );

            CreateBinaryContentRequestDto profileRequest =
                    new CreateBinaryContentRequestDto(
                            mockFile.getOriginalFilename(),
                            mockFile.getSize(),
                            mockFile.getContentType(),
                            mockFile.getBytes()
                    );

            // when & then
            assertThatThrownBy(() -> userService.update(userId, userRequest, profileRequest))
                    .isInstanceOf(EmailAlreadyExistsException.class);

            verify(binaryContentRepository, never()).deleteById(any(UUID.class));
            verify(binaryContentRepository, never()).save(any(BinaryContent.class));
            verify(binaryContentStorage, never()).put(any(UUID.class), any(byte[].class));
            verify(userRepository, never()).save(any(User.class));
            verify(userMapper, never()).toResponseDto(any(User.class));
        }
    }

    @Nested
    @DisplayName("사용자 삭제 테스트")
    class DeleteUserTest {

        @Test
        @DisplayName("정상적으로 사용자를 삭제할 수 있다")
        void deleteUser_Success() {
            // given
            UUID userId = UUID.randomUUID();
            UUID profileId = UUID.randomUUID();
            MockMultipartFile mockFile = mockFile();

            BinaryContent profile = new BinaryContent(
                    mockFile.getOriginalFilename(),
                    mockFile.getSize(),
                    mockFile.getContentType()
            );
            ReflectionTestUtils.setField(profile, "id", profileId);

            User user = new User(
                    "test1",
                    "test1@naver.com",
                    "test1234",
                    profile
            );
            ReflectionTestUtils.setField(user, "id", userId);
            ReflectionTestUtils.setField(user, "status", new UserStatus(user));

            when(userRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(user));

            // when
            userService.delete(userId);

            // then
            verify(userRepository, times(1)).findById(userId);
            verify(binaryContentRepository, times(1)).deleteById(profileId);
            verify(userStatusRepository, times(1)).deleteById(user.getStatus().getId());
            verify(userRepository, times(1)).deleteById(userId);
        }

        @Test
        @DisplayName("존재하지 않는 사용자는 삭제할 수 없다")
        void deleteUser_UserNotFound() {
            // given
            UUID userId = UUID.randomUUID();

            when(userRepository.findById(any(UUID.class)))
                    .thenThrow(UserNotFoundException.class);

            // when & then
            assertThatThrownBy(() -> userService.delete(userId))
                    .isInstanceOf(UserNotFoundException.class);

            verify(userRepository, times(1)).findById(userId);
            verify(binaryContentRepository, never()).deleteById(any(UUID.class));
            verify(userStatusRepository, never()).deleteById(any(UUID.class));
            verify(userRepository, never()).deleteById(any(UUID.class));
        }
    }
}