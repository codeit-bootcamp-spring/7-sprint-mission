package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {
    Map<UUID, ReadStatus> readStatusStore = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        readStatusStore.put(readStatus.getId(), readStatus);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(readStatusStore.get(id));
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusStore.values().stream()
                .filter(r -> r.getUserId().equals(userId) && r.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(readStatusStore.values());
    }

    @Override
    public void update(ReadStatus readStatus) {
        readStatusStore.replace(readStatus.getId(), readStatus);
    }

    @Override
    public void deleteById(UUID id) {
        readStatusStore.remove(id);
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        readStatusStore.values().removeIf(s -> s.getChannelId().equals(channelId));
    }

    @Override
    public void deleteByChannelMember(UUID channelId, UUID memberId) {
        readStatusStore.values().removeIf(s -> s.getChannelId().equals(channelId) &&  s.getUserId().equals(memberId));
    }

    @Override
    public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
        return readStatusStore.values().stream()
                .anyMatch(r -> userId.equals(r.getUserId()) && channelId.equals(r.getChannelId()));
    }

    @Override
    public boolean isExist(UUID readStatusId) {
        return readStatusStore.containsKey(readStatusId);
    }
}
