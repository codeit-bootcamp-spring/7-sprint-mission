package com.sprint.mission.discodeit.repository.file;


import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path MESSAGES_FILE = DATA_DIR.resolve("messages.ser");
    private final Map<UUID, Message> cache = new HashMap<>();

    public FileMessageRepository() {
        try {
            Files.createDirectories(DATA_DIR);
            if(Files.exists(MESSAGES_FILE)) {
                loadAll();
            }
        } catch( IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void loadAll() {
        try(ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(MESSAGES_FILE))) {
            Object obj = ois.readObject();
            if(obj instanceof Map<?, ?> raw ) {
                cache.clear();
                for (Map.Entry<?, ?> entry : raw.entrySet()) {
                    if(entry.getValue() instanceof Message && entry.getKey() instanceof UUID) {
                        cache.put((UUID) entry.getKey(), (Message) entry.getValue());
                    } else {
                        throw new IllegalStateException("Invalid message entry: " + entry.getValue().getClass());
                    }
                }
                return;
            }
            throw new IllegalStateException("Unexpected data format " + obj.getClass());
        } catch (EOFException e) {
          cache.clear();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAll() {
        Path tmp = MESSAGES_FILE.resolveSibling("messages.ser.tmp");
        try(ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(tmp))) {
            oos.writeObject(cache);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        try {
            Files.move(tmp, MESSAGES_FILE, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Message save(Message message) {
        Objects.requireNonNull(message, "Message cannot be null");
        cache.put(message.getId(), message);
        saveAll();
        return message;
    }

    @Override
    public Optional<Message> findById(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        return Optional.ofNullable(cache.get(uuid));
    }

    @Override
    public List<Message> findAll() {
        return new ArrayList<>(cache.values());
    }

    @Override
    public boolean deleteById(UUID uuid) {
        Objects.requireNonNull(uuid, "uuid cannot be null");
        boolean existed = (cache.remove(uuid) != null);
        if(existed) {
            saveAll();
        }
        return existed;
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        Objects.requireNonNull(channelId, "channelId cannot be null");
        return cache.values()
                .stream()
                .filter(m -> m.getChannelId().equals(channelId))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public List<Message> findByAuthorId(UUID authorId) {
        Objects.requireNonNull(authorId, "authorId cannot be null");
        return cache.values()
                .stream()
                .filter(m -> m.getAuthorId().equals(authorId))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public List<Message> findByChannelIdAndAuthorId(UUID channelId, UUID authorId) {
        Objects.requireNonNull(channelId, "channelId cannot be null");
        Objects.requireNonNull(authorId, "authorId cannot be null");
        return cache.values()
                .stream()
                .filter( m -> m.getAuthorId().equals(authorId))
                .filter(m -> m.getChannelId().equals(channelId))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public List<Message> searchByKeyword(String keyword) {
        String k = Objects.requireNonNull(keyword, "keyword cannot be null")
                .toLowerCase()
                .trim();
        if(k.isEmpty()) {
            return new ArrayList<>();
        }
        return cache.values()
                .stream()
                .filter(m -> !m.isDeleted())
                .filter(m -> m.getContent().toLowerCase().contains(k))
                .toList();
    }
}
