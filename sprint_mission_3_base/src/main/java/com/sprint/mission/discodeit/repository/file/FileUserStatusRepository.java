package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class FileUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> storage = new HashMap<>();
    private final Path filePath = Path.of("data/user-status.dat");

    public FileUserStatusRepository() {
        loadFromFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            out.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save UserStatus data", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (!Files.exists(filePath)) return;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(filePath))) {
            Object obj = in.readObject();
            if (obj instanceof Map) {
                storage.clear();
                storage.putAll((Map<UUID, UserStatus>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load UserStatus data", e);
        }
    }

    @Override
    public UserStatus save(UserStatus status) {
        storage.put(status.getId(), status);
        saveToFile();
        return status;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public Optional<UserStatus> findLastSeenByUserId(UUID userId) {
        return storage.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .max(Comparator.comparing(UserStatus::getLastSeenAt));
    }

    @Override
    public List<UUID> findAllUserIdsOnlineWithinMinutes(long minutes) {
        Instant threshold = Instant.now().minus(Duration.ofMinutes(minutes));
        return storage.values().stream()
                .filter(s -> s.getLastSeenAt().isAfter(threshold))
                .map(UserStatus::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
        saveToFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.values().removeIf(s -> s.getUserId().equals(userId));
        saveToFile();
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return storage.values().stream()
                .filter(s -> s.getUserId().equals(userId))
                .findFirst();
    }
    @Override
    public List<UserStatus> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return storage.values().stream()
                .anyMatch(s -> s.getUserId().equals(userId));
    }
}
