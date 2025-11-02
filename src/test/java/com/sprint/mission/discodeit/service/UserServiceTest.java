//package com.sprint.mission.discodeit.service;
package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.dto.Dto_User;
import com.sprint.mission.discodeit.repository.FileUserRepository;
import com.sprint.mission.discodeit.response.Res_User;
import com.sprint.mission.discodeit.service.basic.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUserRepository fileUserRepository;

    private static Dto_User testUserDto;
    private static Long createdUserId;

    // 실제 저장 경로 (.ser 파일)
    private static final String REPO_PATH = "/Users/my05030/Desktop/장미연/7-sprint-mission/repo-data/user/";

    @BeforeAll
    static void beforeAll() throws Exception {
        // 테스트 전에 디렉토리 초기화
        File repoDir = new File(REPO_PATH);
        if (!repoDir.exists()) repoDir.mkdirs();

        // 기존 테스트 파일들 삭제
        for (File f : repoDir.listFiles()) {
            if (f.getName().endsWith(".ser")) f.delete();
        }

        testUserDto = new Dto_User();
        testUserDto.setName("TestUser");
        testUserDto.setEmail("test@example.com");
        testUserDto.setPassword("1234");
    }

    @Test
    @Order(1)
    @DisplayName("1️⃣ 유저 생성 테스트")
    void testCreateUser() {
        Res_User created = userService.createUser(testUserDto);
        assertThat(created).isNotNull();
        assertThat(created.getName()).isEqualTo("TestUser");

        createdUserId = created.getId();

        // 실제 .ser 파일이 생성되었는지 확인
        File expectedFile = new File(REPO_PATH + createdUserId + ".ser");
        assertThat(expectedFile.exists()).isTrue();
    }

    @Test
    @Order(2)
    @DisplayName("2️⃣ 유저 조회 테스트")
    void testReadUser() {
        User user = userService.getUserById(createdUserId);
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("TestUser");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @Order(3)
    @DisplayName("3️⃣ 유저 수정 테스트")
    void testUpdateUser() {
        Dto_User updateDto = new Dto_User();
        updateDto.setName("UpdatedUser");
        updateDto.setEmail("updated@example.com");
        updateDto.setPassword("abcd");

        Res_User updated = userService.updateUser(createdUserId, updateDto);
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo("UpdatedUser");
        assertThat(updated.getEmail()).isEqualTo("updated@example.com");

        // 파일이 여전히 존재해야 함
        File file = new File(REPO_PATH + createdUserId + ".ser");
        assertThat(file.exists()).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("4️⃣ 유저 삭제 테스트")
    void testDeleteUser() {
        boolean deleted = userService.deleteUser(createdUserId);
        assertThat(deleted).isTrue();

        // 파일이 실제로 삭제되었는지 확인
        File file = new File(REPO_PATH + createdUserId + ".ser");
        assertThat(file.exists()).isFalse();
    }

    @AfterAll
    static void cleanup() throws Exception {
        // 테스트 종료 후 남은 .ser 파일 제거
        for (File f : new File(REPO_PATH).listFiles()) {
            if (f.getName().endsWith(".ser")) {
                Files.deleteIfExists(Path.of(f.getPath()));
            }
        }
    }
}


