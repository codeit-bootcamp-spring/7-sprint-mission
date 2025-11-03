package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.*;

import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    boolean existsById(UUID id);
    Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);
    boolean existsByChannelIdAndUserId(UUID channelId, UUID userId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
    void deleteById(UUID id);
    List<com.sprint.mission.discodeit.entity.ReadStatus> findAllByChannelId(UUID channelId);
    void deleteAllByChannelId(UUID channelId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    void deleteAllByUserId(UUID userId);


}
