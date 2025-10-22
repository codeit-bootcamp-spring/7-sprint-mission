package com.sprint.mssion.discodeit.service.file;

import com.sprint.mssion.discodeit.entity.User;
import com.sprint.mssion.discodeit.repository.file.FileManager;
import com.sprint.mssion.discodeit.repository.file.FileUserRepository;
import com.sprint.mssion.discodeit.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/*
add channel removeChannel은 facadeService에서 해야함
 */
class FileUserServiceTest {

    private Path testFilePath;
    private FileManager<User> fileManager;
    private UserService userService;

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = Paths.get("build/tmp/test-filemanager.ser");
        Files.createDirectories(testFilePath.getParent());
        Files.deleteIfExists(testFilePath);
        fileManager = new FileManager<>(testFilePath);
        userService = new FileUserService(
                new FileUserRepository(
                        fileManager
                )
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    @DisplayName("[정상 케이스] - 유저 생성 테스트")
    void givenNewUser_whenCreate_thenSuccess() {
        // given
        User createUser = userService.createUser(
                "username", "email", "010-1111-2222", "pronoun"
        );
        //when
        User result = userService.getUser(createUser.getCommon().getId());

        // then
        assertEquals(createUser, result);
    }

    @Test
    @DisplayName("[비정상케이스 - 없는데이터] - 조회 테스트 ")
    void givenNotExistedUser_whenRead_thenFailed() {
        // given
        UUID invalidID = UUID.randomUUID();

        // when && then
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.getUser(invalidID)
        );

        assertTrue(exception.getMessage().contains("찾을 수 없는 유저"));
    }

    @Test
    @DisplayName("[정상 케이스] - 다건 조회 테스트")
    void givenMultipleUsers_whenRead_thenSuccess() {
        // given
        userService.createUser("username", "email", "010-1111-2222", "pronoun");
        userService.createUser("username2", "email2", "010-1111-2223", "pronoun2");
        userService.createUser("username3", "email3", "010-1111-2224", "pronoun3");

        // when
        List<User> result = userService.getAllUsers();

        // then
        assertThat(result)
                .extracting(User::getUsername, User::getEmail, User::getPhoneNumber,  User::getPronoun)
                .containsExactlyInAnyOrder(
                        tuple("username", "email", "010-1111-2222", "pronoun"),
                        tuple("username2", "email2", "010-1111-2223", "pronoun2"),
                        tuple("username3", "email3", "010-1111-2224", "pronoun3")
                );
    }

    @Test
    @DisplayName("[정상 케이스] - 삭제 테스트")
    void givenUser_whenDelete_thenSuccess() {
        // given
        User user1 = userService.createUser("username", "email", "010-1111-2222", "pronoun");
        User user2 = userService.createUser("username2", "email2", "010-1111-2223", "pronoun2");

        // when && then
        userService.deleteUser(user1.getCommon().getId());

        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> userService.getUser(user1.getCommon().getId())
        );

        assertTrue(exception.getMessage().contains("찾을 수 없는 유저"));
    }



}