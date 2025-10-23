package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.service.*;

import java.util.*;

public class JCFUserService implements UserService {

    private final Map<UUID, User> data = new HashMap<>();

    public JCFUserService() {
    }

    private void validateEmailIsUnique(String email) {
        // 이메일 중복 확인
        boolean isDuplicate = data.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));

        if (isDuplicate) {
            throw new DuplicateEmailException("이미 존재하는 이메일");
        }
    }

    // 생성
    @Override
    public UserInfoDto createUser(String email, String password, String userName, String phoneNum) {
        validateEmailIsUnique(email);
        User newUser = new User(email, password, userName, phoneNum);
        data.put(newUser.getId(), newUser);
        return new UserInfoDto(newUser);

    }
    @Override
    public UserInfoDto createUser(String email, String password, String userName) {
        return createUser(email, password, userName, null);
    }

    // 조회
    @Override
    public Optional<UserInfoDto> findUserInfoById(UUID userId) {
        return Optional.ofNullable(data.get(userId)).map(UserInfoDto::new);
    }

    public Optional<User> findUserEntityById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<UserInfoDto> findAllUsers() {

        return data.values().stream()
                .map(UserInfoDto::new).toList();
    }

    @Override
    public Optional<UserInfoDto> findUserInfoByEmail(String email) {
        User user = data.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        return Optional.ofNullable(user).map(UserInfoDto::new);
    }

    // 수정
    @Override
    public Optional<UserInfoDto> updateProfile(UUID userId, String newUserName, String newPhoneNum) {

        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updateUserName(newUserName);
            user.updatePhoneNum(newPhoneNum);
            return new UserInfoDto(user);
        });
    }

    @Override
    public Optional<UserInfoDto> changePassword(UUID userId, String newPassword) {

        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updatePassword(newPassword);
            return new UserInfoDto(user);
        });
    }

    @Override
    public Optional<UserInfoDto> updateState(UUID userId, UserState newState) {

        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updateState(newState);
            return new UserInfoDto(user);
        });
    }

    // 삭제
    @Override
    public boolean deleteUser(UUID userId) {
        Optional<User> userOptional = Optional.ofNullable(data.get(userId));

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            data.remove(userId);
            System.out.println("유저 삭제 성공");
            return true;
        }
        else {
            System.out.println("해당 유저가 존재하지 않음");
            return false;
        }
    }
}