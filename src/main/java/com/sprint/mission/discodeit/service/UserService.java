package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {
    User creatUser(String name);    // 생성
    void getUser(String name);      // 읽기
    void getAllUsers();             // 모두 읽기
    void updateUser(String name, String reName); // 수정
    void deleteUser(String name);   // 삭제
}
