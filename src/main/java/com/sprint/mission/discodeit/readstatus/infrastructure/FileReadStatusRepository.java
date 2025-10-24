package com.sprint.mission.discodeit.readstatus.infrastructure;

import com.sprint.mission.discodeit.readstatus.application.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import org.springframework.stereotype.Repository;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {

    private final String FILE_PATH = "data/readstatuses.ser"; // 저장 파일 경로

    // 유저 데이터 전체를 파일에서 불러오기
    private Map<UUID, ReadStatus> load() {

        Path filePath = Path.of(FILE_PATH);


        if (Files.notExists(filePath)) {
            return new HashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (Map<UUID, ReadStatus>) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveToFile(Map<UUID, ReadStatus> data) {
        Path filePath = Path.of(FILE_PATH);

        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(filePath))) {
            oos.writeObject(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save(ReadStatus readStatus) {
        Map<UUID, ReadStatus> store = load();
        store.put(readStatus.getId(),readStatus);
        saveToFile(store);
    }

    @Override
    public void remove(ReadStatus readStatus) {
        Map<UUID, ReadStatus> store = load();
        store.remove(readStatus.getId());
        saveToFile(store);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Map<UUID, ReadStatus> store = load();
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReadStatus> findAll() {
        Map<UUID, ReadStatus> store = load();
        return List.copyOf(store.values());
    }
}
