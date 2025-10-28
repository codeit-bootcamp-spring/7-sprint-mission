package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository("JCFReadStatus")
public class JCFReadStatusRepository implements ReadStatusRepository {

    private final Map<UUID, ReadStatus> data;
    public JCFReadStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return Optional.empty();
    }

    @Override
    public void save(UUID bose, UUID id) {

    }


    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {

    }
}
