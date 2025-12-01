/*
package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileUserStatusRepository implements UserStatusRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path USERSTATUS_FILE = DATA_DIR.resolve("userstatus.ser");
    private final Map<UUID, UserStatus> cache = new ConcurrentHashMap<>();

    public FileUserStatusRepository() {
        try {
            Files.createDirectories(DATA_DIR);
            if(Files.exists(USERSTATUS_FILE)) {
                loadAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadAll() {
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(USERSTATUS_FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> raw) {
                cache.clear();
                for(Map.Entry<?, ?> entry : raw.entrySet()) {
                    if(entry.getValue() instanceof UserStatus && entry.getKey() instanceof UUID) {
                        cache.put((UUID) entry.getKey(), (UserStatus) entry.getValue());
                    } else {
                        throw new IllegalStateException("Invalid userstatus entry: " + entry.getValue().getClass());
                    }
                }
                return;
            }
            throw new IllegalStateException("Unexpected object type: " + obj.getClass());
        } catch (EOFException e) {
            cache.clear();
        } catch ( IOException e ) {
            throw new UncheckedIOException(e);
        } catch ( ClassNotFoundException e ) {
            throw new RuntimeException(e);
        }
    }

    private void saveAll() {
        try {
            Path tmp = USERSTATUS_FILE.resolveSibling("userstatus.ser.tmp");
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
                oos.writeObject(cache);
            }
            try {
                Files.move(tmp, USERSTATUS_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmp, USERSTATUS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch ( IOException e ) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Objects.requireNonNull(userStatus);
        cache.put(userStatus.getId(), userStatus);
        saveAll();
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID Id) {
        return Optional.ofNullable(cache.get(Objects.requireNonNull(Id)));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        Objects.requireNonNull(userId);
        return cache.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return cache.values().stream().toList();
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return cache.values().stream()
                .anyMatch(rs -> rs.getUserId().equals(userId));
    }

    @Override
    public boolean deleteById(UUID id) {
        boolean existed = cache.remove(Objects.requireNonNull(id)) != null;
        if(existed) {
            saveAll();
        }
        return existed;
    }
}

 */
