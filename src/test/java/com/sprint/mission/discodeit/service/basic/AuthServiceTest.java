package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.auth.InvalidCredentialsException;
import com.sprint.mission.discodeit.global.exception.auth.InvalidLoginRequestException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("인가 서비스 단위 테스트")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatusRepository userStatusRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BasicAuthService authService;

    @Test
    @DisplayName("정상적으로 사용자가 로그인을 할 수 있다")
    void loginUser_Success() {
        // given
        UUID userId = UUID.randomUUID();
        String username = "test";
        String password = "test1234";

        User user = new User(
                username,
                "test@naver.com",
                password,
                null
        );
        ReflectionTestUtils.setField(user, "id", userId);

        UserStatus userStatus = new UserStatus(user);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        when(userStatusRepository.findByUserId(user.getId()))
                .thenReturn(Optional.of(userStatus));

        when(userStatusRepository.save(userStatus))
                .thenAnswer(i -> i.getArgument(0));

        when(userMapper.toResponseDto(user))
                .thenReturn(mock(UserResponseDto.class));

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when
        UserResponseDto response = authService.login(loginRequestDto);

        // then
        assertThat(response).isNotNull();

        verify(userRepository, times(1)).findByUsername(username);
        verify(userStatusRepository, times(1)).findByUserId(userId);
        verify(userStatusRepository, times(1)).save(userStatus);
        verify(userMapper, times(1)).toResponseDto(user);
    }

    @Test
    @DisplayName("아이디를 입력하지 않으면 로그인을 할 수 없다")
    void loginUser_WithoutUsername_Fail() {
        // given
        UUID userId = UUID.randomUUID();
        String username = "";
        String password = "test1234";

        User user = new User(
                "test",
                "test@naver.com",
                password,
                null
        );
        ReflectionTestUtils.setField(user, "id", userId);

        UserStatus userStatus = new UserStatus(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(InvalidLoginRequestException.class);

        verify(userRepository, never()).findByUsername(username);
        verify(userStatusRepository, never()).findByUserId(userId);
        verify(userStatusRepository, never()).save(userStatus);
        verify(userMapper, never()).toResponseDto(user);
    }

    @Test
    @DisplayName("비밀번호를 입력하지 않으면 로그인을 할 수 없다")
    void loginUser_WithoutPassword_Fail() {
        // given
        UUID userId = UUID.randomUUID();
        String username = "test";
        String password = "";

        User user = new User(
                "test",
                "test@naver.com",
                "test1234",
                null
        );
        ReflectionTestUtils.setField(user, "id", userId);

        UserStatus userStatus = new UserStatus(user);

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(InvalidLoginRequestException.class);

        verify(userRepository, never()).findByUsername(username);
        verify(userStatusRepository, never()).findByUserId(userId);
        verify(userStatusRepository, never()).save(userStatus);
        verify(userMapper, never()).toResponseDto(user);
    }

    @Test
    @DisplayName("아이디가 일치하지 않으면 로그인을 할 수 없다")
    void loginUser_WhenUsernameNotMatched_Fail() {
        // given
        UUID userId = UUID.randomUUID();
        String username = "test12";
        String password = "test1234";

        User user = new User(
                "test",
                "test@naver.com",
                "test1234",
                null
        );
        ReflectionTestUtils.setField(user, "id", userId);

        UserStatus userStatus = new UserStatus(user);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository, times(1)).findByUsername(username);
        verify(userStatusRepository, never()).findByUserId(userId);
        verify(userStatusRepository, never()).save(userStatus);
        verify(userMapper, never()).toResponseDto(user);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 로그인을 할 수 없다")
    void loginUser_WhenPasswordNotMatched_Fail() {
        // given
        UUID userId = UUID.randomUUID();
        String username = "test";
        String password = "test123456";

        User user = new User(
                "test",
                "test@naver.com",
                "test1234",
                null
        );
        ReflectionTestUtils.setField(user, "id", userId);

        UserStatus userStatus = new UserStatus(user);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        LoginRequestDto loginRequestDto = new LoginRequestDto(username, password);

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequestDto))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository, times(1)).findByUsername(username);
        verify(userStatusRepository, never()).findByUserId(userId);
        verify(userStatusRepository, never()).save(userStatus);
        verify(userMapper, never()).toResponseDto(user);
    }
}