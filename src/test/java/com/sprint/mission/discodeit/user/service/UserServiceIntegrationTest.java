package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest

class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() throws IOException {

        userService.resetUserRepository();

    }

    @Test
    @DisplayName("[정상 케이스] 유저 생성")
    @Transactional
    void createUser() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = TestFixture.userCreateFactory();

        //when
        UserDto userDto=userService.createUser(userCreateRequestDto,null);

        //then
        var expectedResult = userRepository.findById(userDto.id()).isPresent();
        assertTrue(expectedResult);
    }

    @Test
    @DisplayName("[예외 케이스] null 유저 생성")
    @Transactional
    void createNullUser() {
        UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(
                null,
                null,
                null
        );

        assertThrows( NullPointerException.class,()->userService.createUser(null,null));
        assertThrows(ConstraintViolationException.class,()-> {
            userService.createUser(userCreateRequestDto, null);
            em.flush();
        });
        };



    @Test
    @DisplayName("[정상 케이스] 유저 삭제")
    @Transactional
    void deleteUser() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = TestFixture.userCreateFactory();
        UserDto userDto = userService.createUser(userCreateRequestDto,null);

        //then
        userService.deleteUser(userDto.id());

        var expectedResult = userRepository.findById(userDto.id()).isEmpty();
        assertTrue(expectedResult);
    }

    @Test
    @DisplayName("[정상 케이스] 유저 전체 조회")
    @Transactional
    void findAllUsers() throws IOException {
        //given
        UserCreateRequestDto userCreateRequestDto = TestFixture.userCreateFactory();
        UserCreateRequestDto userCreateRequestDto2 = TestFixture.userCreateFactory();
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
    @Transactional
    void patchUser() throws IOException {
        //given
        UserDto userDto = userService.createUser(TestFixture.userCreateFactory(),null);
        UserUpdateRequest userPatchRequestDto = TestFixture.userUpdateFactory();

        //when
        UserDto newUser= userService.patchUser(userDto.id(),userPatchRequestDto,null);

        //then
        var actualResult = userService.readUser(userDto.id());

        assertEquals(userPatchRequestDto.newEmail(),actualResult.email());
    }
}