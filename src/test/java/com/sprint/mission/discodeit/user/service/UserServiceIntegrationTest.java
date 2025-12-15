package com.sprint.mission.discodeit.user.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.domain.file.FileByteReadFailException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
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

    @Nested
    @DisplayName("유저 생성 테스트")
    class UserCreateTest {

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
    }

    @Nested
    @DisplayName("유저 변경 테스트")
    class UserPatchTest {
        @Test
        @DisplayName("[정상 케이스] 유저 수정")
        @Transactional
        void patchUser_Success() throws IOException {
            //given
            UserDto userDto = userService.createUser(TestFixture.userCreateFactory(),null);
            UserUpdateRequest userPatchRequestDto = TestFixture.userUpdateFactory();

            //when
            UserDto newUser= userService.patchUser(userDto.id(),userPatchRequestDto,null);

            //then
            var actualResult = userService.readUser(userDto.id());

            assertEquals(userPatchRequestDto.newEmail(),actualResult.email());
        }

        @Test
        @DisplayName("[예외 케이스] 유저 수정 실패")
        @Transactional
        void patchUser_Fail(){
            //given
            UserDto userDto = userService.createUser(TestFixture.userCreateFactory(),null);
            UserUpdateRequest userPatchRequestDto = TestFixture.userUpdateFactory();

            //when
            assertThatThrownBy(()
                    ->userService.patchUser(userDto.id(),userPatchRequestDto,
                    new MockMultipartFile(null,"1111".getBytes())
            )).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("유저 조회 테스트")
    class UserReadTest {

        @Test
        @DisplayName("[정상 케이스] 유저 단일 조회")
        @Transactional(readOnly = true)
        void readUser_Success(){
            //given
            UserCreateRequestDto userCreateRequestDto = TestFixture.userCreateFactory();
            UserDto userDto = userService.createUser(userCreateRequestDto,null);

            //when
            UserDto actualResult = userService.readUser(userDto.id());

            //then
            assertThat(actualResult.id()).isEqualTo(userDto.id());

        }

        @Test
        @DisplayName("[예외 케이스] 존재하지 않는 유저 단일 조회")
        @Transactional(readOnly = true)
        void readNonExistUser_Fail(){

            assertThatThrownBy(()->userService.readUser(UUID.randomUUID()))
                    .isInstanceOf(UserNotExistException.class);

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

    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    class UserDeleteTest {

        @Test
        @DisplayName("[정상 케이스] 유저 삭제")
        @Transactional
        void deleteUser_Success() {
            //given
            UserCreateRequestDto userCreateRequestDto = TestFixture.userCreateFactory();
            UserDto userDto = userService.createUser(userCreateRequestDto,null);

            //then
            userService.deleteUser(userDto.id());

            var expectedResult = userRepository.findById(userDto.id()).isEmpty();
            assertTrue(expectedResult);
        }

        @Test
        @DisplayName("[예외 케이스] 존재하지 않는 유저 삭제")
        @Transactional
        void deleteNonUser_Fail(){

            assertThatThrownBy(
                    ()-> userService.deleteUser(UUID.randomUUID())
            ).isInstanceOf(UserNotExistException.class);

        }
    }

}