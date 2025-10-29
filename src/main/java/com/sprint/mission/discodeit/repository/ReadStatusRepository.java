package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);


    ReadStatus save(UUID userID, UUID channelId);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAll();

    List<ReadStatus> findAllByUserId(UUID userId);

    void deleteById(UUID id);
}
