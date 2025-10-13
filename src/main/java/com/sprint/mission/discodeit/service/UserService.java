package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.UserInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    // 생성
    UserInfo createUser(String email, String password, String userName);

    // 조회
    Optional<UserInfo> findUserInfoById(UUID userId);
    Optional<User> findUserEntityById(UUID userID);
    List<UserInfo> findAllUsers();

    // 수정
    Optional<UserInfo> updateProfile(UUID userId, String newUserName, String newPhoneNum);
    Optional<UserInfo> changePassword(UUID userId, String newPassword);
    Optional<UserInfo> updateState(UUID userId, User.State newState);

    // 삭제
    boolean deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}