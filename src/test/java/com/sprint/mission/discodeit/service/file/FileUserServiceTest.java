package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileUserServiceTest {

    private FileUserService userService;
    private FileUserRepository repository;
    private Path dataDir;

    @BeforeEach
    void setUp() throws IOException {
        repository = new FileUserRepository();
        userService = new FileUserService(repository);

        // 파일 데이터 폴더 경로 확인
        dataDir = Paths.get(System.getProperty("user.dir"), "file-data-map", "User");

        // 테스트 전 폴더 초기화
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        } else {
            Files.createDirectories(dataDir);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // 테스트 후에도 폴더 초기화
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
    }

    @Test
    @DisplayName("Service 메서드 전체 테스트")
    void testAllMethods() {
        // -------------------
        // 1️⃣ create
        // -------------------
        User user1 = userService.create("user1@example.com", "nick1", "pass1");
        User user2 = userService.create("user2@example.com", "nick2", "pass2");

        assertNotNull(user1.getId());
        assertNotNull(user2.getId());

        // 파일 생성 확인
        assertTrue(Files.exists(dataDir.resolve(user1.getId() + ".ser")));
        assertTrue(Files.exists(dataDir.resolve(user2.getId() + ".ser")));

        // -------------------
        // 2️⃣ findAll
        // -------------------
        List<User> allUsers = userService.findAll();
        assertEquals(2, allUsers.size());

        // -------------------
        // 3️⃣ findByEmail
        // -------------------
        User find1 = userService.findByEmail("user1@example.com");
        assertNotNull(find1);
        assertEquals(user1.getId(), find1.getId());

        User find2 = userService.findByEmail("user2@example.com");
        assertNotNull(find2);
        assertEquals(user2.getId(), find2.getId());

        // -------------------
        // 4️⃣ findByNickname
        // -------------------
        List<User> nickList = userService.findByNickname("nick1");
        assertEquals(1, nickList.size());
        assertEquals(user1.getId(), nickList.get(0).getId());

        // -------------------
        // 5️⃣ update
        // -------------------
        User updated = userService.update(user1.getId(), "newNick1", "newPass1");
        assertEquals("newNick1", updated.getNickname());
        assertEquals("newPass1", updated.getPassword());

        // 파일 내용 확인
        User loaded = userService.findByEmail("user1@example.com");
        assertEquals("newNick1", loaded.getNickname());
        assertEquals("newPass1", loaded.getPassword());

        // -------------------
        // 6️⃣ delete
        // -------------------
        User deleted = userService.delete(user2.getId());
        assertEquals(user2.getId(), deleted.getId());

        // 파일이 삭제됐는지 확인
        assertFalse(Files.exists(dataDir.resolve(user2.getId() + ".ser")));

        // 삭제 후 조회
        assertNull(userService.findByEmail("user2@example.com"));

        // findAll 확인
        List<User> remaining = userService.findAll();
        assertEquals(1, remaining.size());
        assertEquals(user1.getId(), remaining.get(0).getId());
    }
}