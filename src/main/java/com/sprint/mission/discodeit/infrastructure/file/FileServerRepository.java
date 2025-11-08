package com.sprint.mission.discodeit.infrastructure.file;

import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import com.sprint.mission.discodeit.domain.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "file")
public class FileServerRepository implements ServerRepository {

    private final String FILE_PATH = "data/servers.ser"; // 저장 파일 경로

    // 유저 데이터 전체를 파일에서 불러오기
    private Map<UUID, Server> load() {

        Path filePath = Path.of(FILE_PATH);


        if (Files.notExists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, Server>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, Server> data) {
        Path filePath = Path.of(FILE_PATH);

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(Server server) {
        Map<UUID, Server> store = load();
        UUID key = server.getId();
        store.put(key, server);
        saveToFile(store);
    }

    public void remove(UUID id) {
        Map<UUID, Server> store = load();
        store.remove(id);
        saveToFile(store);
    }

    public Optional<Server> findById(UUID id) {
        Map<UUID, Server> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Server> findAll() {
        Map<UUID, Server> store = load();
        return List.copyOf(store.values());
    }

}
