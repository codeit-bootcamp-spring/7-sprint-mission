package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(String username, String nickName); // 유저 생성
    User findUser(UUID userId); // 유저 조회
    List<User> findAllUsers(); // 모든 유저 조회
    User updateUser(String username, UUID userId, String nickName); // 유저 업데이트
    void deleteUser(UUID userId); // 유저 삭제
}
