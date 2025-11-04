package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {

    private static final String FILE_PATH = "users.dat";

    @Override
    public User save(User user) {
        List<User> users = findAll();
        users.removeIf(u -> u.getId().equals(user.getId()));
        users.add(user);
        writeToFile(users);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return findAll().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        // ✅ 파일이 없거나 비어 있으면 null 대신 빈 리스트 반환
        List<User> users = readFromFile();
        if (users == null) {
            return new ArrayList<>();
        }
        return users;
    }

    @Override
    public boolean existsById(UUID id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(UUID id) {
        List<User> users = findAll();
        users.removeIf(u -> u.getId().equals(id));
        writeToFile(users);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findAll().stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByUsernameAndPassword(String username, String password) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

    // ---------- 파일 입출력 유틸 ----------

    private List<User> readFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // ✅ 오류 발생 시에도 null 대신 빈 리스트 반환
        }
    }

    private void writeToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
