package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 사용자 관련 비즈니스 로직을 정의하는 서비스 인터페이스입니다.
 */
public interface UserService {

    // 생성
    User createUser(String email, String password, String userName);

    // 조회
    Optional<UserInfo> findUserById(UUID userId);
    List<UserInfo> findAllUsers();

    // 수정
    Optional<User> updateProfile(UUID userId, String newUserName, String newPhoneNum);
    Optional<User> changePassword(UUID userId, String newPassword);
    Optional<User> updateState(UUID userId, User.State newState);

    // 삭제
    boolean deleteUser(UUID userId); // 삭제 메서드 추가 (성공 여부 반환)
}