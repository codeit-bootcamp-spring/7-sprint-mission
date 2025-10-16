package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

public interface UserRepository {
    User creatUser(String name);    // 생성
//    void getUser(String name);      // 읽기
//    void getAllUsers();             // 모두 읽기
    void updateUser(String name, String reName); // 수정
    void deleteUser(String name);
}
