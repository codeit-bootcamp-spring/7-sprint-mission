package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {
    private Path testFilePath;
    private FileManager<User> fileManager;

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = Paths.get("build/tmp/test-filemanager.ser");
        Files.createDirectories(testFilePath.getParent());
        Files.deleteIfExists(testFilePath);
        fileManager = new FileManager<>(testFilePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    @DisplayName("[정상 케이스] - 파일 생성 및 초기화")
    void given_whenCreateFile_thenSuccess() {
        // then
        assertTrue(Files.exists(testFilePath));
    }

    @Test
    @DisplayName("[정상 케이스] - writeFile User 데이터 저장 및 읽기")
    void givenUser_whenWriteFile_thenSuccess() {
        // given
        User user = new User("username", "email", "010-1111-2222", "pronoun");
        Map<UUID, User> users = new HashMap<>();
        users.put(user.getCommon().getId(), user);
        UUID id = user.getCommon().getId();

        // when
        fileManager.writeFile(users);

        // then
        Map<UUID, User> actualResult = fileManager.readFile();
        Assertions.assertThat(actualResult.get(id).hashCode()).isEqualTo(users.get(id).hashCode());
    }

    @Test
    @DisplayName("[정상 케이스] - 여러 User 객체 저장 및 읽기")
    void givenMultipleUsers_whenWriteFile_thenSuccess() {
        // given
        User user1 = new User("username", "email", "010-1111-2222", "pronoun");
        User user2 = new User("username2", "email2", "010-1111-2223", "pronoun2");
        User user3 = new User("username3", "email3", "010-1111-2224", "pronoun3");

        Map<UUID, User> users = new HashMap<>();
        users.put(user1.getCommon().getId(), user1);
        users.put(user2.getCommon().getId(), user2);
        users.put(user3.getCommon().getId(), user3);

        // when
        fileManager.writeFile(users);
        Map<UUID, User> result = fileManager.readFile();

        // then
        assertThat(result.keySet()).isEqualTo(users.keySet());
    }
}
