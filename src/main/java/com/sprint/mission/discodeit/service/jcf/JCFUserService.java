package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data;

    public JCFUserService() {
        this.data = new HashMap<>();
    }

    // 생성
    @Override
    public User createUser(String email, String password, String userName) {
        User newUser = new User(email, password, userName);
        data.put(newUser.getId(), newUser);
        return newUser;
    }

    // 조회
    @Override
    public Optional<User> findUserById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(data.values());
    }

    // 수정
    @Override
    public Optional<User> updateProfile(UUID userId, String newUserName, String newPhoneNum) {
        Optional<User> userOptional = findUserById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.updateUserName(newUserName);
            user.updatePhoneNum(newPhoneNum);
            return Optional.of(user);
        }
        System.out.println("사용자를 찾을 수 없음");
        return Optional.empty();
    }

    @Override
    public Optional<User> changePassword(UUID userId, String newPassword) {
        return findUserById(userId).map(user -> {
            user.updatePassword(newPassword);
            return user;
        });
    }

    @Override
    public Optional<User> updateState(UUID userId, User.State newState) {
        return findUserById(userId).map(user -> {
            user.updateState(newState);
            return user;
        });
    }

    // 삭제
    @Override
    public boolean deleteUser(UUID userId) {
        return false;
    }
}