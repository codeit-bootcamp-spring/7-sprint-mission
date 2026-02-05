package com.sprint.mission.discodeit.integration.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.AuthInvalidCredentialsException;
import com.sprint.mission.discodeit.integration.fixtures.UserFixture;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {

    }

    @Nested
    @DisplayName("checkUser")
    class checkUser {

        @Test
        @DisplayName("[Integration][Flow][Positive] 로그인 - 아이디, 비밀번호 일치 시 회원 정보 반환")
        void login_then_returns_user() {
            User user = UserFixture.createUserWithStatus(userRepository);
            // when
            UserResponseDto loginUser = authService.login(AuthLoginRequestDto.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .build());

            // then
            assertAll(
                    () -> assertEquals(user.getId(), loginUser.id()),
                    () -> assertEquals(user.getUsername(), loginUser.username()),
                    () -> assertEquals(user.getProfile().getId(), loginUser.profile().id()),
                    () -> assertEquals(user.getEmail(), loginUser.email()),
                    () -> assertEquals(true, loginUser.online())
            );
        }


        @Test
        @DisplayName("[Integration][Flow][Negative] 로그인 - 아이디, 비밀번호 불일치 시 AuthInvalidCredentialsException 예외")
        void login_throws_when_not_found() {
            // given
            User user = UserFixture.createUserWithStatus(userRepository);

            // when & then
            assertAll(
                    () -> assertThrows(AuthInvalidCredentialsException.class, () -> authService.login(AuthLoginRequestDto.builder()
                            .username(user.getUsername())
                            .password("wrongPassword")
                            .build())),
                    () -> assertThrows(AuthInvalidCredentialsException.class, () -> authService.login(AuthLoginRequestDto.builder()
                                    .username("wrongName")
                                    .password(user.getPassword())
                                    .build()
                            )
                    )
            );
        }
    }

}
