package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> readStatuses = new ConcurrentHashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        readStatuses.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID uuid) {
        return Optional.ofNullable(readStatuses.get(uuid));
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatuses.values());
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatuses.values().stream()
                .filter(status ->
                        status.getUserId().equals(userId) && status.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readStatuses.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .toList();
    }

    @Override
    public void deleteChannelById(UUID channelId) {
        readStatuses.values()
                .removeIf(status -> status.getChannelId().equals(channelId));
    }

    @Override
    public void delete(UUID uuid) {
        readStatuses.remove(uuid);
    }
}
