/*
package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileReadStatusRepository implements ReadStatusRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path READSTATUS_FILE = DATA_DIR.resolve("readstatus.ser");
    private final Map<UUID, ReadStatus> cache = new ConcurrentHashMap<>();

    public FileReadStatusRepository() {
        try {
            Files.createDirectories(DATA_DIR);
            if(Files.exists(READSTATUS_FILE)) {
                loadAll();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadAll() {
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(READSTATUS_FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> raw) {
                cache.clear();
                for(Map.Entry<?, ?> entry : raw.entrySet()) {
                    if(entry.getValue() instanceof ReadStatus && entry.getKey() instanceof UUID) {
                        cache.put((UUID) entry.getKey(), (ReadStatus) entry.getValue());
                    } else {
                        throw new IllegalStateException("Invalid readstatus entry: " + entry.getValue().getClass());
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
            Path tmp = READSTATUS_FILE.resolveSibling("readstatus.ser.tmp");
            try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
                oos.writeObject(cache);
            }
            try {
                Files.move(tmp, READSTATUS_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                Files.move(tmp, READSTATUS_FILE, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch ( IOException e ) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Objects.requireNonNull(readStatus, "readStatus must not be null");
        cache.put(readStatus.getId(), readStatus);
        saveAll();
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(cache.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return cache.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public boolean deleteById(UUID Id) {
        boolean existed = cache.remove(Objects.requireNonNull(Id)) != null;
        if(existed) {
            saveAll();
        }
        return existed;
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return cache.values()
                .stream()
                .anyMatch(rs -> rs.getUserId().equals(Objects.requireNonNull(userId))
                        && rs.getChannelId().equals(Objects.requireNonNull(channelId)));
    }

    @Override
    public int deleteAllByChannelId(UUID channelId) {
        Objects.requireNonNull(channelId);
        int before = cache.size();
        cache.values()
                .removeIf(rs -> rs.getChannelId().equals(channelId));
        return before - cache.size();
    }
}

 */
