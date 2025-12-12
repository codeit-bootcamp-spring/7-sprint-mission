package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class AuthServiceIntegrationTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;



    @Test
    @DisplayName("[정상 케이스] 유저 로그인")
    void checkLoginUser() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = TestFixture.userCreateFactory();
        UserDto user = userService.createUser(userCreateRequestDto, null);

        //when
        LoginResponseDto loginResponseDto = authService.checkLoginUser(new LoginRequestDto(
                        userCreateRequestDto.username(), userCreateRequestDto.password()
                )
        );
        //then
        var actualResult = loginResponseDto.id();
        var expectedResult = user.id();

        assertEquals(expectedResult,actualResult);
    }


    @Test
    @DisplayName("[예외 케이스] DB에 없는 유저 로그인")
    void checkLoginNotValidUser() throws IOException {

        assertThrows(IllegalArgumentException.class, () -> authService.checkLoginUser(new LoginRequestDto("notValidUser"
                , "notValidPassword")));

    }
}