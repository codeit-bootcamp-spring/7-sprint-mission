package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.*;

public class FileUserRepository implements UserRepository {
    private final String filePath;
    private Map<java.util.UUID, User> data;

    public FileUserRepository(String filePath) {
        this.filePath = filePath;
        this.data = load();
    }

    @SuppressWarnings("unchecked")
    private Map<java.util.UUID, User> load() {
        File f = new File(filePath);
        if (!f.exists()) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            Object obj = ois.readObject();
            return (Map<java.util.UUID, User>) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void persist() {
        File f = new File(filePath);
        f.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        persist();
    }

    @Override
    public User findById(java.util.UUID id) {
        return data.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void delete(java.util.UUID id) {
        data.remove(id);
        persist();
    }
}