package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

/**
 * UserService 기능을 테스트하는 클래스
 */
public class UserServiceTest {
    public static void main(String[] args) {
        // UserService 구현체를 생성
        UserService userService = new JCFUserService();

        // 1. CREATE: User 등록
        User u1 = userService.create("우유", "milk@example.com");
        User u2 = userService.create("철수", "chulsoo@example.com");
        System.out.println("생성된 User:");
        System.out.println(u1.getId() + " / " + u1.getUsername() + " / " + u1.getEmail());
        System.out.println(u2.getId() + " / " + u2.getUsername() + " / " + u2.getEmail());

        // 2. READ: 특정 User 조회
        User found = userService.read(u1.getId());
        System.out.println("\n조회된 User:");
        System.out.println(found.getId() + " / " + found.getUsername());

        // 3. READ ALL: 전체 조회
        List<User> allUsers = userService.readAll();
        System.out.println("\n전체 User:");
        for (User u : allUsers) {
            System.out.println(u.getId() + " / " + u.getUsername());
        }

        // 4. UPDATE: username/email 수정
        userService.updateUsername(u1.getId(), "우유(수정됨)");
        userService.updateEmail(u2.getId(), "chulsoo_new@example.com");

        System.out.println("\n수정 후 User:");
        System.out.println(userService.read(u1.getId()).getUsername());
        System.out.println(userService.read(u2.getId()).getEmail());

        // 5. DEACTIVATE: User 비활성화
        userService.deactivate(u1.getId());
        System.out.println("\n비활성화 후 User:");
        System.out.println(u1.getUsername() + " / active = " + u1.isActive());

        // 6. DELETE: User 삭제
        boolean deleted = userService.delete(u2.getId());
        System.out.println("\n삭제 결과: " + deleted);

        // 삭제 확인
        User checkDeleted = userService.read(u2.getId());
        System.out.println("삭제 확인: " + checkDeleted); // null이어야 함
    }
}
