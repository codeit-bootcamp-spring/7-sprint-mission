package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileReadStatusRepository implements ReadStatusRepository {


    @Override
    public void save(UUID bose, UUID id) {

    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return Optional.empty();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public void deleteById(UUID id) {

    }


}
