package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.UserInfo;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;
    public JCFUserService() {this.data = new HashMap<>();}

    // 생성
    @Override
    public User createUser(String email, String password, String userName) {
        validateEmailIsUnique(email);
        User newUser = new User(email, password, userName);
        data.put(newUser.getId(), newUser);
        return newUser;
    }
    // 이메일 중복 확인
    private void validateEmailIsUnique(String email) {
        boolean isDuplicate = data.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));

        if (isDuplicate) {
            throw new DuplicateEmailException("이미 존재하는 이메일");
        }
    }
    // 조회

    @Override
    public Optional<UserInfo> findUserById(UUID userId) {
        return Optional.ofNullable(data.get(userId)).map(UserInfo::new);
    }
    public Optional<User> findEntityById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<UserInfo> findAllUsers() {

        return data.values().stream()
                .map(UserInfo::new).toList();
    }

    // 수정
    @Override
    public Optional<UserInfo> updateProfile(UUID userId, String newUserName, String newPhoneNum) {
        Optional<User> userOptional = Optional.ofNullable(data.get(userId));

        return userOptional.map(user -> {
            user.updateUserName(newUserName);
            user.updatePhoneNum(newPhoneNum);
            return new UserInfo(user);
        });
    }

    @Override
    public Optional<UserInfo> changePassword(UUID userId, String newPassword) {
        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updatePassword(newPassword);
            return new UserInfo(user);
        });
    }

    @Override
    public Optional<UserInfo> updateState(UUID userId, User.State newState) {
        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updateState(newState);
            return new UserInfo(user);
        });
    }

    // 삭제
    @Override
    public boolean deleteUser(UUID userId) {
        if (data.remove(userId) != null) {
            System.out.println("삭제 성공");
            return true;
        }
        else {
            System.out.println("삭제 실패");
            return false;
        }
    }
}