package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
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
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
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

        verify(userRepository,times(1)).save(ArgumentCaptor.forClass(User.class).capture());
        verify(binaryContentRepository,times(1)).save(ArgumentCaptor.forClass(BinaryContent.class).capture());
        verify(userStatusRepository,times(1)).save(ArgumentCaptor.forClass(UserStatus.class).capture());
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

        verify(userRepository,times(1)).save(ArgumentCaptor.forClass(User.class).capture());
        verify(binaryContentRepository,times(1)).save(ArgumentCaptor.forClass(BinaryContent.class).capture());


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

        when(userRepository.existsById(any(UUID.class))).thenReturn(true);
        doNothing().when(userRepository).deleteById(any(UUID.class));

        userService.deleteUser(UUID.randomUUID());

        verify(userRepository,times(1)).deleteById(ArgumentCaptor.forClass(UUID.class).capture());
    }

    @Test
    @DisplayName("[예외 케이스] 유저 삭제 실패")
    void deleteUser_Fail() {

        assertThatThrownBy(()->userService.deleteUser(UUID.randomUUID()))
                .isInstanceOf(UserNotExistException.class);

        then(userRepository).should(never()).deleteById(any(UUID.class));
    }
}
