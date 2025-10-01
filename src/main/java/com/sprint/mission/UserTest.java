package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.UUID;

public class UserTest {

    public static void read(UUID id) {
        System.out.println(userService.findUserById(id));
    }
    public static void readAll() {
        List<UserInfo> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("유저 없음");
        }
        else users.forEach(System.out::println);
    }

    static UserService userService = new JCFUserService();

    public static void main(String[] args) {

        User user1 = null;

        // --- 사용자 생성 ---
        System.out.println("--- 사용자 생성 ---");
        try {
            user1 = userService.createUser
                    ("test@codeit.com", "QWERty1!", "admin");
            System.out.println("생성 성공");
        } catch (InvalidInputException e) {
            System.out.println("생성 실패: " + e.getMessage());
            return;
        }

        // --- 사용자 조회 ---
        UUID userId = user1.getId();
        System.out.println("--- 사용자 조회 ---");
        System.out.println(userService.findUserById(userId));

        // --- 정보 수정 후 조회 ---
        System.out.println("--- 수정 후 다시 조회 ---");

        userService.updateProfile(userId, "adminUpdate", "010-1234-5678");
        read(userId);

        // --- 삭제 ---
        System.out.println("\n--- 사용자 삭제 ---");
        userService.deleteUser(userId);

        // --- 전체 조회 ---
        System.out.println("--- 사용자 목록 ---");
        readAll();
        System.out.println("종료");
        System.out.println(user1);
    }
}

// 고쳐야할것 -> update시에 전화번호, 닉네임, 비밀번호의 유효성검사