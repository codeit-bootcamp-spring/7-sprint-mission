package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.Optional;
import java.util.UUID;

public interface InterfaceReadStatusRepository extends BaseInterfaceRepository<ReadStatus> {
    Optional<ReadStatus> findByUserAndChannelId(UUID userID, UUID channelID);
}
