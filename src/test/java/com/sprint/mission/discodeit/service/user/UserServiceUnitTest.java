package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.domain.file.FileByteReadFailException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.assertj.core.api.Assertions;
import org.instancio.When;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Test")
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private BasicUserService userService;

    private User user1;
    private BinaryContent binaryContent1;
    private UserStatus userStatus1;
    private UserDto userDto1;

    @BeforeEach
    void setUp() {
        user1 = User.createUserFactory("Ronaldo","siuuu@gmail.com","1234");
        binaryContent1 = new BinaryContent("siuuFile","text/plain",10L);
         userStatus1 = new UserStatus(user1, Instant.now());
        userDto1 = new UserDto(
                user1.getId(),
                user1.getUserName(),
                user1.getEmail(),
                null,
                null,
                true
        );
    }

    @Test
    @DisplayName("[정상 케이스] 유저 생성")
    void createUser_Success() {

        when(binaryContentRepository.save(any(BinaryContent.class)))
                .thenReturn(binaryContent1);

        when(binaryContentStorage.put(any(),any(byte[].class)))
                .thenReturn(binaryContent1.getId());

        when(userRepository.save(any(User.class)))
                .thenReturn(user1);
        when(userStatusRepository.save(any(UserStatus.class)))
                .thenReturn(userStatus1);

        when(userMapper.toDto(any(User.class)))
        .thenReturn(userDto1);

        UserDto response = userService.createUser(new UserCreateRequestDto(
                user1.getUserName(),
                user1.getEmail(),
                user1.getPassword()
                )
                , new MockMultipartFile("file",
                        "test.txt", "text/plain", "Hello, World!".getBytes()));


        assertThat(response).isEqualTo(userDto1);

        verify(userRepository,times(1)).save(any(User.class));
        verify(binaryContentRepository,times(1)).save(any(BinaryContent.class));
        verify(userStatusRepository,times(1)).save(any(UserStatus.class));
    }

    @Test
    @DisplayName("[실페 케이스] 유저 생성 실패(Runtime Exception)")
    void createUser_Fail() {

        when(binaryContentRepository.save(any(BinaryContent.class)))
                .thenReturn(binaryContent1);

        when(binaryContentStorage.put(any(),any(byte[].class)))
                .thenThrow(RuntimeException.class);

        assertThatThrownBy(()-> userService.createUser(new UserCreateRequestDto(
                        "아무거나",
                        "some@EMail",
                        "1234"
                )
                , new MockMultipartFile("file",
                        "test.txt", "text/plain", "Hello, World!".getBytes())))
                .isInstanceOf(FileByteReadFailException.class);

        verify(userRepository,never()).save(any(User.class));

    }

    @Test
    @DisplayName("[정상 케이스] 유저 변경")
    void updateUser_Success() {

        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(user1));
        when(binaryContentRepository.save(any(BinaryContent.class)))
                .thenReturn(binaryContent1);
        when(userRepository.save(any(User.class)))
                .thenReturn(user1);
        when(userMapper.toDto(any(User.class)))
                .thenReturn(userDto1);

        UserDto response = userService.patchUser(UUID.randomUUID(), new UserUpdateRequest(
                "newnew",
                "hello@hello",
                "nono"
        ), new MockMultipartFile("file",
                "test.txt", "text/plain", "Hello, World!".getBytes()));

        assertThat(response).isEqualTo(userDto1);

        verify(userRepository,times(1)).save(any(User.class));
        verify(binaryContentRepository,times(1)).save(any(BinaryContent.class));


    }

    @Test
    @DisplayName("[예외 케이스] 유저 변경 실패")
    void updateUser_Fail() {

        when(userRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(user1));
        when(binaryContentRepository.save(any(BinaryContent.class)))
                .thenReturn(binaryContent1);
        when(binaryContentStorage.put(any(),any(byte[].class)))
                .thenThrow(RuntimeException.class);

        assertThatThrownBy(()->userService.patchUser(UUID.randomUUID(), new UserUpdateRequest(
                "newnew",
                "hello@hello",
                "nono"
        ), new MockMultipartFile("file",
                "test.txt", "text/plain", "Hello, World!".getBytes())))
                .isInstanceOf(FileByteReadFailException.class);

        verify(userRepository,never()).save(any(User.class));

    }

    @Test
    @DisplayName("[정상 케이스] 유저 삭제 ")
    void deleteUser_Success() {

        doNothing().when(userRepository).deleteById(any(UUID.class));
        userService.deleteUser(UUID.randomUUID());
        verify(userRepository,times(1)).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("[예외 케이스] 유저 삭제 실패")
    void deleteUser_Fail() {

        assertThatThrownBy(()->userService.deleteUser(UUID.randomUUID()))
                .isInstanceOf(UserNotExistException.class);

    }
}
