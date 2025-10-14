package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.UserInfo;
import com.sprint.mission.discodeit.exception.*;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserTest {

    // 유저 조회
    public static void read(UUID id) {
        userService.findUserInfoById(id)
                .ifPresentOrElse(System.out::println, () -> System.out.println("유저를 찾을 수 없음"));
    }
    // 유저 모두 조회
    public static void readAll() {
        List<UserInfo> users = userService.findAllUsers();
        if (users.isEmpty()) {
            System.out.println("유저를 찾을 수 없음");
        }
        else users.forEach(System.out::println);
    }

    static UserService userService = new JCFUserService();



    // 메인
    public static void main(String[] args) {

        UserInfo user1 = null;

        // --- 사용자 생성 ---
        System.out.println("--- 사용자 생성 ---");
        try {
            user1 = userService.createUser
                    ("test@codeit.com", "QWERty1!", "admin");
            System.out.println("생성 성공");
        } catch (InvalidInputException | DuplicateEmailException e) {
            System.out.println("생성 실패: " + e.getMessage());
            return;
        }

        // --- 사용자 조회 ---
        UUID userId = user1.getId();
        System.out.println("--- 사용자 조회 ---");
        read(userId);

        // --- 정보 수정 후 조회 ---
        System.out.println("--- 수정 후 다시 조회 ---");

        try{
            userService.updateProfile(userId, "adminUpdate", "010-1234-5678");
        } catch (InvalidInputException e) {
            System.out.println("업데이트 실패: " + e.getMessage());
        }
        read(userId);

        // --- 삭제 ---
        System.out.println("\n--- 사용자 삭제 ---");
        userService.deleteUser(userId);

        // --- 전체 조회 ---
        System.out.println("--- 사용자 목록 ---");
        readAll();
        System.out.println("시스템 종료");
    }
}