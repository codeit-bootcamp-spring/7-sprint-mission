package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.global.util.file.FileManager;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.nio.file.Path;
import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path filePath;
    private final Map<UUID, ReadStatus> readStatuses;

    public FileReadStatusRepository(@Value("${file.path.readStatusPath}") Path readStatusFilePath) {
        this.filePath = readStatusFilePath;
        FileManager.init(filePath);
        this.readStatuses = FileManager.readFile(filePath);
    }

    @Override
    public void save(ReadStatus readStatus) {
        readStatuses.put(readStatus.getId(), readStatus);
        FileManager.writeFile(filePath, readStatuses);
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusId) {
        return Optional.ofNullable(readStatuses.get(readStatusId));
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatuses.values());
    }

    @Override
    public void deleteById(UUID readStatusId) {
        readStatuses.remove(readStatusId);
    }

    @Override
    public boolean existsById(UUID readStatusId) {
        return readStatuses.containsKey(readStatusId);
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatuses.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();

    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        readStatuses.values().removeIf(
                readStatus -> readStatus.getChannelId().equals(channelId));
        FileManager.writeFile(filePath, readStatuses);
    }


}
