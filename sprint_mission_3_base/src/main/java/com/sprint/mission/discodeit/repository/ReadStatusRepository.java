package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.*;

import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);
    boolean existsByChannelIdAndUserId(UUID channelId, UUID userId);
    void deleteById(UUID id);
}
