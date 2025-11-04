package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "jcf"
)
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data;

    public JCFReadStatusRepository() {
        this.data = new ConcurrentHashMap<>();
    }

    @Override
    public ReadStatus save(UUID userId, UUID channelId) {
        // 새로운 ReadStatus 생성 (ReadStatus에 생성자가 있다고 가정)
        ReadStatus readStatus = new ReadStatus(userId,channelId);
        data.put(readStatus.getId(), readStatus);
        return readStatus;
    }

    @Override
    public ReadStatus find(UUID readStatusId) {
        return data.get(readStatusId);
    }

    @Override
    public List<ReadStatus> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId)
                        && rs.getChannelId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return data.values().stream()
                .filter(rs -> rs.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        data.remove(id);
    }


}

