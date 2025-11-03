package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> storage = new HashMap<>();
    private final Path filePath = Path.of("data/binary-content.dat");

    public FileBinaryContentRepository() {
        loadFromFile();
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            out.writeObject(storage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save BinaryContent data", e);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFromFile() {
        if (!Files.exists(filePath)) return;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(filePath))) {
            Object obj = in.readObject();
            if (obj instanceof Map) {
                storage.clear();
                storage.putAll((Map<UUID, BinaryContent>) obj);
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load BinaryContent data", e);
        }
    }

    @Override
    public BinaryContent save(BinaryContent content) {
        storage.put(content.getId(), content);
        saveToFile();
        return content;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return storage.values().stream()
                .filter(b -> ids.contains(b.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<BinaryContent> findAllByMessageId(UUID messageId) {
        return storage.values().stream()
                .filter(b ->
                        b.getOwnerType().name().equals("MESSAGE_ATTACHMENT") &&
                                Objects.equals(b.getOwnerId(), messageId)
                )
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        storage.remove(id);
        saveToFile();
    }

    @Override
    public void deleteByUserId(UUID userId) {
        storage.values().removeIf(b ->
                b.getOwnerType().name().equals("USER_PROFILE") &&
                        Objects.equals(b.getOwnerId(), userId)
        );
        saveToFile();
    }

    @Override
    public void deleteAllByMessageId(UUID messageId) {
        storage.values().removeIf(b ->
                b.getOwnerType().name().equals("MESSAGE_ATTACHMENT") &&
                        Objects.equals(b.getOwnerId(), messageId)
        );
        saveToFile();
    }

    @Override
    public Optional<BinaryContent> findByUserId(UUID userId) {
        return storage.values().stream()
                .filter(b ->
                        b.getOwnerType().name().equals("USER_PROFILE") &&
                                Objects.equals(b.getOwnerId(), userId)
                )
                .findFirst();
    }

}
