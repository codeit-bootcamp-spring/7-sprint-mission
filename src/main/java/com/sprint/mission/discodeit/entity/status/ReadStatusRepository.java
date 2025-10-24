package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.repository.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends BaseRepository<ReadStatus> {

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    // List<ReadStatus>
}
