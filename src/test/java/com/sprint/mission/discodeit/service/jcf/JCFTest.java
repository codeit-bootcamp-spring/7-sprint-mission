package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JCFUserServiceTest {
    JCFUserService userService = JCFUserService.getInstance();

    @Test
    void serviceTest() {
        User tester1 = userService.insert(new User("최지혜", "aaaa@naver.com", "1234"));
        userService.insert(new User("홍길동", "bbb@naver.com", "1agfhk"));

        userService.update(tester1.getId(), "홍길동", "000");
        userService.delete(tester1.getId());

        System.out.println(userService.findAll());
    }
}