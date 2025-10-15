package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.application.repository.UserRepository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final String FILE_PATH = "data/users.ser"; // 저장 파일 경로

    // 유저 데이터 전체를 파일에서 불러오기
    private Map<UUID, User> load() {


        Path filePath = Path.of(FILE_PATH);
        try {
            // 파일 없으면 빈 맵 반환
            if (Files.notExists(filePath)) {
                return new HashMap<>();
            }


            // try-with-resources로 안전하게 파일 읽기
            try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
                return (Map<UUID, User>) ois.readObject();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    // 유저 데이터를 파일로 저장하기
    private void saveToFile(Map<UUID, User> data) {
        Path filePath = Path.of(FILE_PATH);
        try {
            try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                oos.writeObject(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void save(User user) {
        Map<UUID, User> store = load();
        UUID key = user.getId();
        store.put(key, user);
        saveToFile(store);
    }

    public void remove(User user) {
        Map<UUID, User> store = load();
        UUID userId = user.getId();
        store.remove(userId);
        saveToFile(store);
    }

    public Optional<User> findById(UUID id) {
        Map<UUID, User> store = load();
        return Optional.ofNullable(store.get(id));
    }


    public Optional<User> findByEmail(String email) {
        Map<UUID, User> store = load();
        return store.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();

    }


    @Override
    public List<User> findAll() {
        Map<UUID, User> store = load();
        return List.copyOf(store.values().stream().toList());
    }
}
