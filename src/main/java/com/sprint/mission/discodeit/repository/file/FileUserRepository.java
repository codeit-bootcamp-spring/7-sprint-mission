package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserState;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileUserRepository implements UserRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERS_FILE = DATA_DIR.resolve("users.ser");
    private final Map<UUID, User> cache = new ConcurrentHashMap<>();

    public FileUserRepository() {
        try {
            Files.createDirectories(DATA_DIR);
            if(Files.exists(USERS_FILE)) {
                loadAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadAll() {
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(USERS_FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> raw) {
                cache.clear();
                for(Map.Entry<?, ?> entry : raw.entrySet()) {
                    if(entry.getKey() instanceof UUID && entry.getValue() instanceof User) {
                        cache.put((UUID) entry.getKey(), (User) entry.getValue());
                    } else {
                        throw new IllegalStateException("Invalid key: " + entry.getKey());
                    }
                }
                return;
            }
            throw new IllegalStateException("Unexpected data format: " + obj.getClass());
        } catch (EOFException e) {
            cache.clear();
        }catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAll() {
        try {
            Path tmp = USERS_FILE.resolveSibling("users.ser.tmp");
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
                oos.writeObject(cache);
            }
            try {
                Files.move(tmp, USERS_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmp, USERS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public User save(User user) {
        Objects.requireNonNull(user, "User must not be null");
        cache.put(user.getId(), user);
        saveAll();
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<User> findAll() {
        return cache.values().stream().toList();
    }

    @Override
    public boolean deleteById(UUID id) {
        Objects.requireNonNull(id, "id cannot be null");
        boolean exists = (cache.remove(id) != null);
        if(exists) { saveAll();}
        return exists;
    }

    @Override
    public List<User> findByName(String username) {
        String n = Objects.requireNonNull(username, "Username cannot be null").trim();
        return cache.values()
                .stream()
                .filter(u -> u.getUsername().equals(n))
                .toList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String e = Objects.requireNonNull(email, "Email cannot be null")
                .trim().toLowerCase();
        return cache.values()
                .stream()
                .filter(u -> u.getEmail().trim().toLowerCase().equals(e))
                .findFirst();
    }
}
