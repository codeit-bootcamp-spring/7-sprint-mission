package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FileReadStatusRepository extends AbstractFileRepository<ReadStatus, UUID> implements ReadStatusRepository {

    private static final String FILE_PATH = "data" + File.separator + "readStatus.ser";

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }

    @Override
    protected UUID getId(ReadStatus readStatus) {
        return readStatus.getId();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return findAll().stream()
                .filter(readStatus ->readStatus.getUserId().equals(userId)
                && readStatus.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return findAll().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteChannelById(UUID channelId) {
        Map<UUID, ReadStatus> data = loadData();

        List<UUID> list = data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .map(ReadStatus::getId)
                .toList();

        list.forEach(data::remove);
        saveData(data);


    }
}
