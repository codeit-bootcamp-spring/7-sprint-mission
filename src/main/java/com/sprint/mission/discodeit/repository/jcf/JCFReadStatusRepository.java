package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFReadStatusRepository implements ReadStatusRepository {
    private final Map<UUID, ReadStatus> data = new ConcurrentHashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Objects.requireNonNull(readStatus, "readStatus must not be null");
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        Objects.requireNonNull(userId, "userId must not be null");
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .toList();
    }

    @Override
    public boolean deleteById(UUID Id) {
        return data.remove(Objects.requireNonNull(Id)) != null;
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values()
                .stream()
                .anyMatch(rs -> rs.getUserId().equals(Objects.requireNonNull(userId))
                && rs.getChannelId().equals(Objects.requireNonNull(channelId)));
    }

    @Override
    public int deleteAllByChannelId(UUID channelId) {
        Objects.requireNonNull(channelId);
        int before = data.size();
        data.values()
                .removeIf(rs -> rs.getChannelId().equals(channelId));
        return before - data.size();
    }
}
