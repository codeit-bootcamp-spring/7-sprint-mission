package com.sprint.mission.discodeit.entity.status.repository.file;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.entity.status.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.file.FileBaseRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileReadStatusRepository extends FileBaseRepository<ReadStatus> implements ReadStatusRepository {

    private static final String READ_STATUS_DATA_FILE = "readStatusData.ser";

    public FileReadStatusRepository(String basePath) {
        super(basePath + "/" + READ_STATUS_DATA_FILE);
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(readStatus ->
                        readStatus.getUserId().equals(userId) &&
                                readStatus.getChannelId().equals(channelId)
                ).findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        data.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
        saveData();
    }

    @Override
    public void deleteAllByUserIdAndChannelId(UUID userId, UUID channelId) {
        data.values().removeIf(readStatus ->
                readStatus.getChannelId().equals(channelId) && readStatus.getUserId().equals(userId));
        saveData();
    }
}
