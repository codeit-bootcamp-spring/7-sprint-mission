//package com.sprint.mission.discodeit.service;
package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_User;
import com.sprint.mission.discodeit.entity.dto.UserDto;
import com.sprint.mission.discodeit.entity.dto.Res_User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.basic.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private FileUserRepository fileUserRepository;

    private static Dto_User testUserDto;
    private static UUID createdUserId;

    // 실제 저장 경로 (.ser 파일)
    private static final String REPO_PATH = "/Users/my05030/Desktop/장미연/7-sprint-mission/repo-data/USER/";

    @BeforeAll
    static void beforeAll() throws Exception {
        // 테스트 전에 디렉토리 초기화
        File repoDir = new File(REPO_PATH);
        if (!repoDir.exists()) repoDir.mkdirs();

        // 기존 테스트 파일들 삭제
        for (File f : repoDir.listFiles()) {
            if (f.getName().endsWith(".ser")) f.delete();
        }

        testUserDto = Dto_User.from("TestUser", "test@example.com", "1234");
    }

    @Test
    @Order(1)
    @DisplayName("1️⃣ 유저 생성 테스트")
    void testCreateUser() {
        Dto_BinaryContent dtoBinaryContent =  Dto_BinaryContent.from("haha", "txt", new byte[0], 0L);
        Res_User created = userService.create(testUserDto, Optional.of(dtoBinaryContent));
        createdUserId = created.userId();

        assertThat(created).isNotNull();
        assertThat(created.userName()).isEqualTo("TestUser");
    }

    @Test
    @Order(2)
    @DisplayName("2️⃣ 유저 조회 테스트")
    void testFindUser() {
        Dto_BinaryContent dtoBinaryContent =  Dto_BinaryContent.from("haha", "txt", new byte[0], 0L);
        Dto_User dtoUser = Dto_User.from("⭐️ 별", "1234", "별@example.com");
        Res_User created = userService.create(dtoUser, Optional.of(dtoBinaryContent));
        UserDto user = userService.find(created.userId());
        assertThat(user).isNotNull();
        assertThat(user.username()).isEqualTo("⭐️ 별");
        assertThat(user.email()).isEqualTo("별@example.com");
    }

    @Test
    @Order(3)
    @DisplayName("3️⃣ 유저 수정 테스트")
    void testUpdateUser() {
        Dto_BinaryContent dtoBinaryContent =  Dto_BinaryContent.from("haha", "txt", new byte[0], 0L);
        Dto_User dtoUser = Dto_User.from("⭐️ 별", "1234", "별@example.com");
        Res_User created = userService.create(dtoUser, Optional.of(dtoBinaryContent));

        Dto_User dtoUser_II = Dto_User.from("🐯호랭이는 어흥", "호랭123", "어흥이@email.com");
        Res_User updated = userService.update(created.userId(), dtoUser_II, Optional.of(dtoBinaryContent));
        assertThat(updated).isNotNull();
        assertThat(updated.userName()).isEqualTo("🐯호랭이는 어흥");
        assertThat(updated.eMail()).isEqualTo("어흥이@email.com");

        // 파일이 여전히 존재해야 함
//        File file = new File(REPO_PATH + createdUserId + ".ser");
//        assertThat(file.exists()).isTrue();
    }

    @Test
    @Order(4)
    @DisplayName("4️⃣ 유저 삭제 테스트")
    void testDeleteUser() {
        Dto_BinaryContent dtoBinaryContent =  Dto_BinaryContent.from("haha", "txt", new byte[0], 0L);
        Dto_User dtoUser = Dto_User.from("⭐️ 별", "1234", "별@example.com");
        Res_User created = userService.create(dtoUser, Optional.of(dtoBinaryContent));
        userService.delete(created.userId());
//        assertThat(deleted).isTrue();

        // 파일이 실제로 삭제되었는지 확인
//        File file = new File(REPO_PATH + createdUserId + ".ser");
//        assertThat(file.exists()).isFalse();
    }

//    @AfterAll
    static void cleanup() throws Exception {
        // 테스트 종료 후 남은 .ser 파일 제거
        Path DIRECTORY = Paths.get(System.getProperty("user.dir"), "repo-data/USER");
        // 삭제할 디렉토리 객체 생성
        File directoryToDelete = new File(DIRECTORY.toAbsolutePath().toString());
        // 재귀적으로 삭제
        FileSystemUtils.deleteRecursively(directoryToDelete);
    }
}


