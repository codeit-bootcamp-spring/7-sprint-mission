package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository extends BaseRepository<ReadStatus> {

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    // List<ReadStatus>
}
