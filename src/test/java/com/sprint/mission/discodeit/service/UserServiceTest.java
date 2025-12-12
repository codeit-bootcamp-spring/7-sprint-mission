package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentManager;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserDto;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import com.sprint.mission.discodeit.service.mapper.UserStatusMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 테스트")
class UserServiceTest {

    @Spy
    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);
    @Spy
    private final UserStatusMapper userStatusMapper = Mappers.getMapper(UserStatusMapper.class);
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private BinaryContentManager binaryContentManager;
    @Mock
    private ReadStatusRepository readStatusRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("유저 정보 업데이트 성공 - 파일 포함")
    void updateUserInfoSuccessWithFile() {
        //given
        UUID userId = UUID.randomUUID();

        User user = new User("test@gmail.com", "1234", "test");
        BinaryContent oldProfile = new BinaryContent(null, null, 10);
        user.updateProfile(oldProfile);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        UserUpdateRequest updateRequest =
                new UserUpdateRequest("new@gmail.com", "new", "4321");

        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "profile.png",
                "image/png",
                "fake image".getBytes()
        );

        BinaryContent newProfile = new BinaryContent(null, null, 20);
        when(binaryContentManager.saveFileAndMeta(mockFile))
                .thenReturn(newProfile);

        //when
        UserDto result = userService.updateUserInfo(userId, updateRequest, mockFile);

        //then
        assertThat(result.getEmail()).isEqualTo("new@gmail.com");
        assertThat(result.getUsername()).isEqualTo("new");
        assertThat(user.getPassword()).isEqualTo(updateRequest.newPassword());
        assertThat(user.getProfile()).isEqualTo(newProfile);
        verify(binaryContentManager).deleteFile(oldProfile);
        verify(binaryContentManager).saveFileAndMeta(mockFile);
    }

    @Nested
    @DisplayName("유정 생성")
    class createUser {

        @Test
        @DisplayName("유저 생성 성공")
        void createUserSuccess() {
            //given
            UserCreateRequest request = new UserCreateRequest("test@gmail.com", "1234", "test");
            when(userRepository.existsByEmailOrUsername("test@gmail.com", "test"))
                    .thenReturn(false);
            User user = new User("test@gmail.com", "1234", "test");
            when(userRepository.save(any(User.class)))
                    .thenReturn(user);
            BinaryContent binaryContent = new BinaryContent(null, null, 1);

            when(channelRepository.findAllByType(ChannelType.PUBLIC))
                    .thenReturn(Collections.emptyList());
            //when
            UserDto userDto = userService.createUser(request, null);

            //then
            assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
            assertThat(userDto.getUsername()).isEqualTo(user.getUsername());
            assertThat(userDto.getProfile()).isEqualTo(user.getProfile());
            verify(binaryContentManager, never()).saveFileAndMeta(any(MultipartFile.class));
            verify(readStatusRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("유저 생성 성공 - 파일 첨부")
        void createUserSuccessProfile() {
            //given
            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",                      // 파라미터 이름
                    "test.png",                  // 파일명
                    "image/png",                 // Content-Type
                    "fake image data".getBytes() // 파일 내용 (byte[])
            );
            UserCreateRequest request = new UserCreateRequest("test@gmail.com", "1234", "test");
            when(userRepository.existsByEmailOrUsername("test@gmail.com", "test"))
                    .thenReturn(false);
            User user = new User("test@gmail.com", "1234", "test");
            when(userRepository.save(any(User.class)))
                    .thenReturn(user);
            BinaryContent binaryContent = new BinaryContent(null, null, 1);
            when(binaryContentManager.saveFileAndMeta(mockFile))
                    .thenReturn(binaryContent);
            when(channelRepository.findAllByType(ChannelType.PUBLIC))
                    .thenReturn(Collections.emptyList());

            user.updateProfile(binaryContent);

            //when
            UserDto userDto = userService.createUser(request, mockFile);

            //then
            assertThat(userDto.getEmail()).isEqualTo(user.getEmail());
            assertThat(userDto.getUsername()).isEqualTo(user.getUsername());

            verify(binaryContentManager).saveFileAndMeta(any(MultipartFile.class));
            verify(readStatusRepository).saveAll(anyList());
        }

        @Test
        @DisplayName("유저 생성 실패 - 이메일 또는 username 중복")
        void createUserFailDuplicate() {
            // given
            UserCreateRequest request =
                    new UserCreateRequest("test@gmail.com", "1234", "test");

            when(userRepository.existsByEmailOrUsername("test@gmail.com", "test"))
                    .thenReturn(true);

            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "test.png",
                    "image/png",
                    "data".getBytes()
            );

            // when & then
            assertThatThrownBy(() -> userService.createUser(request, mockFile))
                    .isInstanceOf(UserAlreadyExistsException.class);

            verify(binaryContentManager, never()).saveFileAndMeta(any());
            verify(userRepository, never()).save(any());
            verify(readStatusRepository, never()).saveAll(anyList());
        }
    }

    @Nested
    @DisplayName("유저 수정")
    class UpdateUser {
        @Test
        @DisplayName("유저 정보 업데이트 성공 - 파일 포함")
        void updateUserInfoSuccessWithFile() {
            //given
            UUID userId = UUID.randomUUID();

            User user = new User("test@gmail.com", "1234", "test");
            BinaryContent oldProfile = new BinaryContent(null, null, 10);
            user.updateProfile(oldProfile);

            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));

            UserUpdateRequest updateRequest =
                    new UserUpdateRequest("new@gmail.com", "new", "4321");

            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "profile.png",
                    "image/png",
                    "fake image".getBytes()
            );

            BinaryContent newProfile = new BinaryContent(null, null, 20);
            when(binaryContentManager.saveFileAndMeta(mockFile))
                    .thenReturn(newProfile);

            //when
            UserDto result = userService.updateUserInfo(userId, updateRequest, mockFile);

            //then
            assertThat(result.getEmail()).isEqualTo("new@gmail.com");
            assertThat(result.getUsername()).isEqualTo("new");
            assertThat(user.getPassword()).isEqualTo(updateRequest.newPassword());
            assertThat(user.getProfile()).isEqualTo(newProfile);
            verify(binaryContentManager).deleteFile(oldProfile);
            verify(binaryContentManager).saveFileAndMeta(mockFile);
        }
        @Test
        @DisplayName("유저 정보 업데이트 실패")
        void updateUserInfoFail() {
            //given
            UUID userId = UUID.randomUUID();
            when(userRepository.findById(userId))
                    .thenReturn(Optional.empty());

            UserUpdateRequest updateRequest =
                    new UserUpdateRequest("new@gmail.com", "new", "4321");

            MockMultipartFile mockFile = new MockMultipartFile(
                    "file",
                    "profile.png",
                    "image/png",
                    "fake image".getBytes()
            );

            //when
            assertThatThrownBy(()-> userService.updateUserInfo(userId, updateRequest, mockFile))
                    .isInstanceOf(UserNotFoundException.class);

            //then
            verify(binaryContentManager, never()).deleteFile(any(BinaryContent.class));
            verify(binaryContentManager, never()).saveFileAndMeta(any(MultipartFile.class));
        }
    }

    @Nested
    @DisplayName("유저 삭제")
    class DeleteUser {

        @Test
        @DisplayName("유저 삭제 성공")
        void deleteUserSuccess (){
            //given
            UUID id = UUID.randomUUID();
            User user = new User("test@gmail.com", "1234", "test");
            BinaryContent profile = new BinaryContent(null, null, 10);
            user.updateProfile(profile);

            when(userRepository.findById(id))
                    .thenReturn(Optional.of(user));

            //when
            userService.deleteUser(id);

            //then
            verify(userRepository).delete(user);
            verify(binaryContentManager).deleteFile(profile);
         }

        @Test
        @DisplayName("유저 삭제 실패")
        void deleteUserFail (){
            //given
            UUID id = UUID.randomUUID();

            when(userRepository.findById(id))
                    .thenReturn(Optional.empty());

            //when
            assertThatThrownBy(()-> userService.deleteUser(id))
                    .isInstanceOf(UserNotFoundException.class);

            //then
            verify(userRepository, never()).delete(any(User.class));
            verify(binaryContentManager, never()).deleteFile(any(BinaryContent.class));
        }
    }
}