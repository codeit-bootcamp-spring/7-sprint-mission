package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFReadStatusRepository implements ReadStatusRepository {
    //Status 데이터
    private final Map<UUID, ReadStatus> data = new ConcurrentHashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID statusId) {
        return Optional.ofNullable(data.get(statusId));
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(readStatus -> readStatus.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void update(UUID statusId) {
        data.get(statusId).update();
    }

    @Override
    public void delete(UUID statusId) {
        data.remove(statusId);
    }

    @Override
    public boolean existsById(UUID statusId) {
        return data.containsKey(statusId);
    }
}
