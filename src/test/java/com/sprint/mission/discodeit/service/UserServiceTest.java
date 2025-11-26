package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.util.TestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestFixture fixture;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    void setUp() throws IOException {

        userService.resetUserRepository();

    }

    @Test
    @DisplayName("[정상 케이스] 유저 생성")
    void createUser() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = fixture.userCreateFactory();

        //when
        UserDto userDto=userService.createUser(userCreateRequestDto,null);

        //then
        var expectedResult = userRepository.findById(userDto.id()).isPresent();
        assertTrue(expectedResult);
    }


    @Test
    @DisplayName("[정상 케이스] 유저 삭제")
    void deleteUser() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = fixture.userCreateFactory();
        UserDto userDto = userService.createUser(userCreateRequestDto,null);

        //then
        userService.deleteUser(userDto.id());

        var expectedResult = userRepository.findById(userDto.id()).isEmpty();
        assertTrue(expectedResult);
    }

    @Test
    @DisplayName("[정상 케이스] 유저 전체 조회")
    void findAllUsers() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = fixture.userCreateFactory();
        UserCreateRequestDto userCreateRequestDto2 = fixture.userCreateFactory();
        userService.createUser(userCreateRequestDto,null);
        userService.createUser(userCreateRequestDto2,null);

        //when
        List<UserDto> users = userService.findAllUsers();
        List<User> usersFromRepository = userRepository.findAll();

        //then
        assertEquals(usersFromRepository.size(),users.size());
    }

    @Test
    @DisplayName("[정상 케이스] 유저 수정")
    void patchUser() throws IOException {
        //given
        UserDto userDto = userService.createUser(fixture.userCreateFactory(),null);
        UserUpdateRequest userPatchRequestDto = fixture.userUpdateFactory();

        //when
        UserDto newUser= userService.patchUser(userDto.id(),userPatchRequestDto,null);

        //then
        var actualResult = userService.readUser(userDto.id());

        assertEquals(userPatchRequestDto.newEmail(),actualResult.email());
    }
}