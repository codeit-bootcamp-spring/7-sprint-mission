package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileUserRepository implements UserRepository {

    private final String FILE_PATH = "users_data.dat";
    private Map<UUID, User> users;

    public FileUserRepository() {
        this.users = loadData();
    }

    @SuppressWarnings("unchecked")
    private Map<UUID, User> loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ConcurrentHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ConcurrentHashMap<>();
        }
    }

    private synchronized void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(this.users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        saveData();
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return users.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteById(UUID id) {
        if (users.remove(id) != null) {
            saveData();
        }
    }
}