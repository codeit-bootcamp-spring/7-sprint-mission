package com.sprint.mission.discodeit.service.login;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BasicAuthServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BasicAuthServiceTest.class);
    @Autowired
    private BasicAuthService basicAuthService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("[정상 케이스] - 로그인 유저 확인")
    void checkLoginUser() {
        //given
        String id = "testId";
        String pw ="testPw";

        var user = userRepository.saveUser(User.builder().userName(id).name("테스트 유저").email("").password(pw).build());
        //when
        var actualResult = basicAuthService.checkLoginUser(new LoginRequestDto(id,pw));

        //then
        assertThat(actualResult.getName()).isEqualTo(user.getName());

    }

    @Test
    @DisplayName("[예외 케이스] - 로그인 유저 확인 실패")
    void checkLoginUserFail() {
        //given
        String id = "testId";
        String pw ="testPw";
        var user = userRepository.saveUser(User.builder().userName(id).name("테스트 유저").email("").password("diffpw").build());

        //when
        var exception =assertThrows(IllegalArgumentException.class,()->basicAuthService.checkLoginUser(new LoginRequestDto(id,pw)));

    }

    @Test
    @DisplayName("[예외 케이스] - Null 삽입")
            void check_null_input()
    {
        var user = userRepository.saveUser(Instancio.create(User.class));
        //when & then

        assertThatThrownBy(()->
                basicAuthService.checkLoginUser(null))
                .isInstanceOf(NullPointerException.class);

    }
}