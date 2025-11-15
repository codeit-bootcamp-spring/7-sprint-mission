package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository

public class JCFReadStatusRepository implements ReadStatusRepository {

    private  final Map<UUID, ReadStatus> store = new HashMap<>();



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
