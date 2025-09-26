package com.sprint.mission.discodeit; // Main 클래스를 위한 패키지 (요청에 따라 com.sprint.mission 하위에 위치)

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.Optional;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        UserService userService = new JCFUserService();

        System.out.println("--- 1. 등록 (Create) ---");
        User user1 = new User("Alice", "alice@test.com");
        User user2 = new User("Bob", "bob@test.com");

        userService.create(user1);
        userService.create(user2);
        System.out.println("등록된 User: " + userService.readAll().size() + "명");

        UUID aliceId = user1.getId();

        System.out.println("\n--- 2. 조회 (Read) ---");
        System.out.println("Alice 단건 조회: " + userService.read(aliceId).orElse(null));
        System.out.println("모든 User 조회: " + userService.readAll());

        System.out.println("\n--- 3. 수정 (Update) ---");
        // 새로운 User 객체를 '수정 데이터'로 활용
        User updatePayload = new User("Alice_New", "alice.updated@test.com");
        Optional<User> updatedAlice = userService.update(aliceId, updatePayload);

        System.out.println("\n--- 4. 수정된 데이터 조회 ---");
        Optional<User> aliceVerify = userService.read(aliceId);
        System.out.println("수정 후 Alice 조회: " + aliceVerify.orElse(null));

        System.out.println("\n--- 5. 삭제 (Delete) ---");
        boolean deleteResult = userService.delete(aliceId);
        System.out.println("Alice 삭제 결과: " + (deleteResult ? "성공" : "실패"));

        System.out.println("\n--- 6. 조회를 통해 삭제 확인 ---");
        System.out.println("Alice 조회 (삭제 후): " + userService.read(aliceId).isPresent());
        System.out.println("남은 User 수: " + userService.readAll().size());
    }
}