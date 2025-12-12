package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.binarycontent.BinaryContentManager;
import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
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
}