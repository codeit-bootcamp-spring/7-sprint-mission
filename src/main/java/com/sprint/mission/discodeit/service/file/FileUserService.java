package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.entity.dto.UserInfo;
import com.sprint.mission.discodeit.exception.DuplicateEmailException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static final long serialVersionUID = 1L;

    public static final String ROOT_PATH
            = "/Users/apple/Desktop/codeit-7th-develop/DiscodeitUpload";

    private ChannelService channelService;

    private Map<UUID, User> data = new HashMap<>();

    public FileUserService() {
        File file = new File(ROOT_PATH);
        if (!file.exists()) {
            file.mkdir();       // 생성해야 할 폴더 경로가 하나일 때
        }
        this.data = loadUserData();
    }

    private Map<UUID, User> loadUserData() {
        try (ObjectInputStream ois
                     = new ObjectInputStream(
                new FileInputStream(ROOT_PATH + "/userData.ser"))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("데이터 로딩 실패");
        }
    }

    private void saveUserData() {
        try (ObjectOutputStream oos
                     = new ObjectOutputStream(
                new FileOutputStream(ROOT_PATH + "/userData.ser"))) {
            oos.writeObject(data);
            System.out.println("데이터 저장 성공");
        } catch (Exception e) {
            throw new RuntimeException("데이터 저장 실패");
        }
    }

    //==========================================생성==

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
    public UserInfo createUser(String email, String password, String userName, String phoneNum) {
        validateEmailIsUnique(email);
        User newUser = new User(email, password, userName, phoneNum);
        data.put(newUser.getId(), newUser);

        saveUserData();
        return new UserInfo(newUser);
    }

    @Override
    public UserInfo createUser(String email, String password, String userName) {
        return createUser(email, password, userName, null);
    }

    // 조회
    @Override
    public Optional<UserInfo> findUserInfoById(UUID userId) {
        return Optional.ofNullable(data.get(userId)).map(UserInfo::new);
    }

    public Optional<User> findUserEntityById(UUID userId) {
        return Optional.ofNullable(data.get(userId));
    }

    @Override
    public List<UserInfo> findAllUsers() {

        return data.values().stream()
                .map(UserInfo::new).collect(Collectors.toList());
    }

    @Override
    public Optional<UserInfo> findUserInfoByEmail(String email) {
        User user = data.values().stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
        return Optional.ofNullable(user).map(UserInfo::new);
    }

    // 수정
    @Override
    public Optional<UserInfo> updateProfile(UUID userId, String newUserName, String newPhoneNum) {

        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updateUserName(newUserName);
            user.updatePhoneNum(newPhoneNum);
            saveUserData();
            return new UserInfo(user);
        });
    }

    @Override
    public Optional<UserInfo> changePassword(UUID userId, String newPassword) {

        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updatePassword(newPassword);
            saveUserData();
            return new UserInfo(user);
        });
    }

    @Override
    public Optional<UserInfo> updateState(UUID userId, UserState newState) {

        return Optional.ofNullable(data.get(userId)).map(user -> {
            user.updateState(newState);
            saveUserData();
            return new UserInfo(user);
        });
    }

    // 논리 삭제
    @Override
    public boolean deleteUser(UUID userId) {

        return findUserEntityById(userId).map(user -> {
            user.softDelete();
            System.out.println("유저 삭제 성공");
            saveUserData();
            return true;
        }).orElseGet(() -> {
            System.out.println("해당 유저가 존재하지 않음");
            return false;
        });
    }
}
