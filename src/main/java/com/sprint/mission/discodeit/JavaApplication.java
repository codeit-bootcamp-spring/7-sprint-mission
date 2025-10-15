package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import java.util.Optional;
import java.util.UUID;

public class JavaApplication {

    /**
     * 애플리케이션의 시작점입니다. JCFUserService를 초기화하고 테스트를 실행합니다.
     * @param args 커맨드 라인 인자 (사용되지 않음)
     */
    public static void main(String[] args) {
        // JCFUserService를 사용하도록 초기화
        UserService userService = new JCFUserService();
        System.out.println("--- 🚀 Running with JCF (Java Collection Framework) Service ---");

        // UserService를 인자로 받아 테스트를 실행하는 메서드 호출
        runUserTest(userService);
    }

    /**
     * 주어진 UserService 구현체를 사용하여 CRUD 작업을 테스트하는 메서드입니다.
     * @param userService 테스트에 사용할 UserService 구현체
     */
    public static void runUserTest(UserService userService) {
        // createdUserId를 null로 초기화하지 않고, 나중에 할당하기 위해 선언만 합니다.
        UUID createdUserId;

        System.out.println("\n--- 1. 등록 (Create) ---");
        // User 생성 시 생성자에 맞게 (username, email) 순서로 전달
        User user1 = new User("Alice", "alice@test.com");
        User user2 = new User("Bob", "bob@test.com");

        User createdUser1 = userService.create(user1);
        userService.create(user2);
        createdUserId = createdUser1.getId(); // Alice의 ID 저장

        System.out.println("등록된 User: " + userService.readAll().size() + "명");

        System.out.println("\n--- 2. 조회 (Read) ---");
        System.out.println("Alice 단건 조회: " + userService.read(createdUserId).orElse(null));
        System.out.println("모든 User 조회: " + userService.readAll());

        System.out.println("\n--- 3. 수정 (Update) ---");
        // 새로운 User 객체를 '수정 데이터'로 활용 (이름을 Alice_New로 변경)
        User updatePayload = new User("Alice_New", "alice.updated@test.com");
        Optional<User> updatedAlice = userService.update(createdUserId, updatePayload);

        // 🚨 getName() 대신 getUsername() 사용
        updatedAlice.ifPresentOrElse(
                user -> System.out.println("수정 성공: " + user.getUsername() + "로 변경됨"),
                () -> System.out.println("수정 실패: User not found.")
        );

        System.out.println("\n--- 4. 수정된 데이터 조회 ---");
        Optional<User> aliceVerify = userService.read(createdUserId);
        System.out.println("수정 후 Alice 조회: " + aliceVerify.orElse(null));

        System.out.println("\n--- 5. 삭제 (Delete) ---");
        boolean deleteResult = userService.delete(createdUserId);
        System.out.println("Alice 삭제 결과: " + (deleteResult ? "성공" : "실패"));

        System.out.println("\n--- 6. 조회를 통해 삭제 확인 ---");
        Optional<User> aliceAfterDelete = userService.read(createdUserId);
        System.out.println("Alice 조회 (삭제 후, 존재 여부): " + aliceAfterDelete.isPresent());
        System.out.println("남은 User 수: " + userService.readAll().size());

        System.out.println("\n--- Test finished for " + userService.getClass().getSimpleName() + " ---");
    }
}