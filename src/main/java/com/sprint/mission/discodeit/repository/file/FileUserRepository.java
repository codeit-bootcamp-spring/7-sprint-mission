package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;
import com.sprint.mission.discodeit.service.file.ReadService;
import com.sprint.mission.discodeit.service.file.LoadService;


public class FileUserRepository implements UserRepository {

    private static final String filename = "users";

    //로드 세이브
    private List<User> loadAll() {
        List<User> list = ReadService.read(filename, User.class);
        return (list != null) ? list : new ArrayList<>();
    }
    private void saveAll(List<User> list) {
        LoadService.load(filename, list);
    }

    @Override
    public User create(String userId, String password, String userName, String userNickname) {
        User user = new User(userId, password, userName, userNickname);
        List<User> users = loadAll();
        users.add(user);
        saveAll(users);
        return user;
    }

    @Override
    public User read(UUID userId) {
        return loadAll().stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<User> readAll() {
        return loadAll();
    }

    @Override
    public boolean delete(UUID userId) {
        List<User> users = loadAll();
        boolean removed = users.removeIf(u -> u.getId().equals(userId));
        if (removed) saveAll(users);
        return removed;
    }

    @Override
    public User updateName(UUID uuid, String userName) {
        List<User> users = loadAll();
        User u = users.stream()
                .filter(x -> x.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
        u.setUserName(userName);
        saveAll(users);
        return u;
    }

    @Override
    public User updateNickName(UUID uuid, String userNickname) {
        List<User> users = loadAll();
        User u = users.stream()
                .filter(x -> x.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
        u.setUserNickname(userNickname);
        saveAll(users);
        return u;
    }

    @Override
    public User update(UUID uuid, Consumer<User> updater) {
        List<User> users = loadAll();
        User u = users.stream()
                .filter(x -> x.getId().equals(uuid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("고유넘버 없다: " + uuid));
        updater.accept(u);
        saveAll(users);
        return u;
    }
}