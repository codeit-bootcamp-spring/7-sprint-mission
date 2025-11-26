package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.login.LoginResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.service.util.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    AuthService authService;

    @Autowired
    UserService userService;

    @Autowired
    TestFixture fixture;

    @Test
    @DisplayName("[정상 케이스] 유저 로그인")
    void checkLoginUser() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = fixture.userCreateFactory();
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

        //given
        UserCreateRequestDto userCreateRequestDto = fixture.userCreateFactory();
        UserDto user = userService.createUser(userCreateRequestDto, null);

        //when
        LoginResponseDto loginResponseDto = authService.checkLoginUser(new LoginRequestDto(
                        userCreateRequestDto.username(), userCreateRequestDto.password()
                )
        );
        //then

    }
}