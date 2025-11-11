package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.repository.ReadStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository

public class JCFReadStatusRepository implements ReadStatusRepository {

    private  final Map<UUID, ReadStatus> store = new HashMap<>();

    public JCFReadStatusRepository() {
        UUID userId = UUID.fromString("32121212-1212-1212-1212-121212343434");
        UUID channelId = UUID.fromString("02121212-1212-1212-1212-121212343434");
        ReadStatus readStatus = new ReadStatus(userId, channelId, Instant.now());
        UUID readStatusId = UUID.fromString("82121212-1212-1212-1212-121212343434");
        store.put(readStatusId,readStatus);
    }

    @Override
    public void save(ReadStatus readStatus) {
        UUID key = readStatus.getId();
        store.put(key, readStatus);
    }

    @Override
    public void remove(UUID id) {
        store.remove(id);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<ReadStatus> findAll() {
        return List.copyOf(store.values());
    }
}
