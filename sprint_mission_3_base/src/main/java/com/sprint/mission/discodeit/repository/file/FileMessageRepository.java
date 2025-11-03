package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository implements MessageRepository {

    private static final Path DIRECTORY = Paths.get("data/messages");
    private static final String EXTENSION = ".bin";

    public FileMessageRepository() {
        try {
            Files.createDirectories(DIRECTORY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path pathOf(UUID id) {
        return DIRECTORY.resolve(id.toString() + EXTENSION);
    }

    @Override
    public Message save(Message m) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(Files.newOutputStream(pathOf(m.getId()),
                             StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(m);
            return m;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Path p = pathOf(id);
        if (!Files.exists(p)) return Optional.empty();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p))) {
            return Optional.of((Message) ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ 채널 기준 조회 (findAll 제거)
    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        try {
            if (!Files.exists(DIRECTORY)) return List.of();
            return Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .map(p -> {
                        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p))) {
                            return (Message) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .filter(m -> channelId.equals(m.getChannelId()))
                    .sorted(Comparator.comparing(Message::getCreatedAt)) // 필요하면 정렬
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(UUID id) {
        try {
            Files.deleteIfExists(pathOf(id));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // ✅ 채널 기준 일괄 삭제
    @Override
    public void deleteAllByChannelId(UUID channelId) {
        try {
            if (!Files.exists(DIRECTORY)) return;
            Files.list(DIRECTORY)
                    .filter(p -> p.toString().endsWith(EXTENSION))
                    .forEach(p -> {
                        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(p))) {
                            Message m = (Message) ois.readObject();
                            if (channelId.equals(m.getChannelId())) {
                                Files.deleteIfExists(p);
                            }
                        } catch (IOException | ClassNotFoundException ignored) { }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(pathOf(id));
    }
}
