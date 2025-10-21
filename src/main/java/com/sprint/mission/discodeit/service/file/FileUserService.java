package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.*;

public class FileUserService implements UserService {

    private static final String USER_FILE_PATH = "users.ser";
    private static final File FILE = new File(USER_FILE_PATH);

    // 데이터 저장 Map 필드
    private final Map<UUID, User> userMap;

    // 생성자에서 Map을 초기화하여, 파일을 한 번 불러옴
    public FileUserService() {
        this.userMap = loadData();
    }

    private Map<UUID, User> loadData(){

        try (FileInputStream load = new FileInputStream(USER_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(load)) {

            return (Map<UUID, User>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, User> data) {
        try (FileOutputStream fos = new FileOutputStream(USER_FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User createUser(String username, String nickName) {
        User newUser = new User(username, nickName);
        UUID userId = newUser.getId();
        userMap.put(userId, newUser);
        saveData(userMap);
        return newUser;
    }
    @Override
    public User findUser(UUID userId) {
        return userMap.get(userId);
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User updateUser(String username, UUID userId, String nickName) {
        User user = userMap.get(userId);
        if(username != null && !username.isEmpty()){
            user.setUsername(username);
        }
        if(nickName != null && !nickName.isEmpty()){
            user.setNickName(nickName);
        }
        saveData(userMap);
        return user;
    }

    @Override
    public void deleteUser(UUID userId) {
        userMap.remove(userId);
        saveData(userMap);
    }
}
