package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.impl.UserServiceImpl;
import com.sprint.mission.discodeit.utils.AppConfigRegacy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class UserView {

    private final UserService userService;
    private final Scanner sc;
    private final SharedView sharedView;

    public void showUserMenu() {
        while (true) {
            System.out.println("\n--- 유저 관리 ---");
            System.out.println("1. 유저 추가");
            System.out.println("2. 유저 프로필 수정");
            System.out.println("3. 유저 상태 변경");
            System.out.println("4. 비밀번호 변경");
            System.out.println("5. 유저 논리적 삭제");
            System.out.println("6. 유저 물리적 삭제");
            System.out.println("7. 모든 유저 조회");
            System.out.println("8. 활성 유저 조회");
            System.out.println("9. 유저 이름으로 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 유저 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        updateUserProfile();
                        break;
                    case 3:
                        changeUserState();
                        break;
                    case 4:
                        changePassword();
                        break;
                    case 5:
                        softDeleteUser();
                        break;
                    case 6:
                        deleteUser();
                        break;
                    case 7:
                        userService.findAll().forEach(System.out::println);
                        break;
                    case 8:
                        userService.findAllNonDel().forEach(System.out::println);
                        break;
                    case 9:
                        findUserByUsername();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println("작업 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private void createUser() {
        System.out.print("사용자 이름(ID): ");
        String username = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();
        System.out.print("이메일: ");
        String email = sc.nextLine();
        System.out.print("닉네임: ");
        String nickname = sc.nextLine();
        User newUser = userService.createUser(username, password, email, nickname, null);
        System.out.println("사용자 생성 완료: " + newUser);
    }

    private void updateUserProfile() {
        User user = selectUserFromList(true);
        if (user == null) return;
        System.out.print("새 닉네임: ");
        String newNickname = sc.nextLine();
        System.out.print("새 이메일: ");
        String newEmail = sc.nextLine();
        User updatedUser = userService.updateProfile(user.getId(), newNickname, newEmail, null);
        System.out.println("프로필 수정 완료: " + updatedUser);
    }

    private void changeUserState() {
        System.out.println("--- 상태를 변경할 사용자 선택 ---");
        User user = sharedView.selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- 변경할 상태 선택 ---");
        System.out.println("1. 온라인");
        System.out.println("2. 오프라인");
        System.out.println("3. 자리비움");
        System.out.println("4. 다른 용무 중");
        System.out.print(">> 상태 번호: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1: userService.goOnline(user.getId()); break;
            case 2: userService.goOffline(user.getId()); break;
            case 3: userService.setAway(user.getId()); break;
            case 4: userService.setDoNotDisturb(user.getId()); break;
            default: System.out.println("잘못된 선택입니다."); return;
        }
        System.out.println("사용자 상태 변경 완료: " + userService.findById(user.getId()).getState().getDescription());
    }

    private void changePassword() {
        System.out.println("--- 비밀번호를 변경할 사용자 선택 ---");
        User user = sharedView.selectUserFromList(true);
        if (user == null) return;

        System.out.print("새 비밀번호 (8자 이상): ");
        String newPassword = sc.nextLine();

        userService.changePassword(user.getId(), newPassword);
        System.out.println("비밀번호 변경이 완료되었습니다.");
    }

    private void softDeleteUser() {
        User user = selectUserFromList(true);
        if (user == null) return;
        userService.softDeleteById(user.getId());
        System.out.println("사용자 논리적 삭제 완료.");
    }

    private void deleteUser() {
        User userToDelete = selectUserFromList(false);
        if (userToDelete == null) return;
        userService.deleteById(userToDelete.getId());
        System.out.println("사용자(" + userToDelete.getUsername() + ") 물리적 삭제 완료.");
    }

    private void findUserByUsername() {
        System.out.print("조회할 사용자 이름: ");
        String username = sc.nextLine();
        User foundUser = userService.findByUsernameNonDel(username);
        System.out.println("조회 결과: " + foundUser);
    }

    // 공통 헬퍼 메서드
    public User selectUserFromList(boolean nonDeletedOnly) {
       return sharedView.selectUserFromList(nonDeletedOnly);
    }
}