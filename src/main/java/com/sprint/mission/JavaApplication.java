package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.update.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.*;


public class JavaApplication {

    public static void main(String[] args) {

        UserService userService = new JCFUserService();

        System.out.println("User Method");

        // 유저1, 2 생성
        User u1 = new User("admin@codeit.com", "00000000", "admin");
        User u2 = new User("asd@codeit.com", "qwer1234", "codeitSB7");
        User u3 = new User("wan8441@naver.com", "12345678", "gnara0719");
        userService.create(u3);
        userService.create(u1);
        userService.create(u2);
        System.out.println("유저1, 2, 3 생성");

        // 전체 읽기
        System.out.println("전체 유저: " + userService.readAll());

        UserUpdate uu = new UserUpdate();
        uu.updateUserName("adminUpdate");
        // 수정
        userService.update(u1.getId(), uu);
//        userService.updateState(u1.getId(), User.State.OFFLINE);

        // 수정 후 읽기
        System.out.println("유저1 업데이트: " + userService.read(u1.getId()));

        // 유저 삭제 후 전체 읽기
        userService.delete(u2.getId());
        System.out.println("유저2 삭제");
        System.out.println("전체 유저: " + userService.readAll());


    }
}