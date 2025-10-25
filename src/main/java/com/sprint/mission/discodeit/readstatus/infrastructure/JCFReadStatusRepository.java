package com.sprint.mission.discodeit.readstatus.infrastructure;

import com.sprint.mission.discodeit.readstatus.application.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;

import java.util.*;

public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> store = new HashMap<>();

    @Override
    public void save(ReadStatus readStatus) {
        UUID key = readStatus.getId();
        store.put(key,readStatus);
    }

    @Override
    public void remove(ReadStatus readStatus) {
        UUID key = readStatus.getId();
        store.remove(key);
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
