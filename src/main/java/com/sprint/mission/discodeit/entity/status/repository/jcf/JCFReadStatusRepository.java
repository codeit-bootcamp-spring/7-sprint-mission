package com.sprint.mission.discodeit.entity.status.repository.jcf;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.entity.status.repository.ReadStatusRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data = new HashMap<>();

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
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
    }

    @Override
    public void deleteAllByUserIdAndChannelId(UUID userId, UUID channelId) {
        data.values().removeIf(readStatus ->
                readStatus.getChannelId().equals(channelId) && readStatus.getUserId().equals(userId));
    }
}
