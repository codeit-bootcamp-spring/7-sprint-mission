package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JCFUserServiceTest {
    private JCFUserRepository userRepository;
    private JCFUserService userService;

    @BeforeEach
    void setUp() {
        userRepository = JCFUserRepository.getInstance();
        userService = JCFUserService.getInstance();

        userRepository.findAll().forEach(u -> userRepository.delete(u.getId()));
    }

    @Test
    @DisplayName("유저 생성 및 저장")
    void create() {
        User user = userService.create("test@example.com", "테스트닉", "1234");

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("테스트닉", user.getNickname());

        // data Map 안에 들어갔는지 확인
        assertEquals(1, userRepository.findAll().size());
        assertEquals(user, userRepository.findById(user.getId()));
    }

    @Test
    @DisplayName("전체 유저 목록 조회")
    void findAll() {
        userService.create("a@example.com", "A", "pw");
        userService.create("b@example.com", "B", "pw");

        List<User> allUsers = userService.findAll();
        assertEquals(2, allUsers.size());
    }

    @Test
    @DisplayName("이메일로 유저 조회")
    void findByEmail() {
        User user = userService.create("findme@example.com", "닉네임", "pw");

        User found = userService.findByEmail("findme@example.com");

        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
    }

    @Test
    @DisplayName("닉네임으로 유저 조회")
    void findByNickname() {
        User user = userService.create("nick@example.com", "닉테스트", "pw");

        List<User> foundList = userService.findByNickname("닉테스트");

        assertNotNull(foundList);
        assertFalse(foundList.isEmpty());
        assertEquals(user.getId(), foundList.get(0).getId());
    }

    @Test
    @DisplayName("유저 삭제")
    void delete() {
        User user = userService.create("del@example.com", "삭제테스트", "pw");
        UUID id = user.getId();

        User deleted = userService.delete(id);

        assertNotNull(deleted);
        assertEquals(id, deleted.getId());

        // Map에서 제거됐는지 확인
        assertNull(userRepository.findById(id));
        assertEquals(0, userRepository.findAll().size());
    }

    @Test
    @DisplayName("유저 업데이트")
    void update() {
        User user = userService.create("up@example.com", "OldName", "oldpw");
        UUID id = user.getId();

        User updated = userService.update(id, "NewName", "newpw");

        assertNotNull(updated);
        assertEquals("NewName", updated.getNickname());
        assertEquals("newpw", updated.getPassword());

        // Map 안에 있는 객체도 같이 업데이트됐는지 확인
        User stored = userRepository.findById(id);
        assertEquals("NewName", stored.getNickname());
        assertEquals("newpw", stored.getPassword());
    }
}