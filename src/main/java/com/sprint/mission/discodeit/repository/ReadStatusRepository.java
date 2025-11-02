package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus status);

    Optional<ReadStatus> findById(UUID id);

    /** 사용자-채널 쌍은 보통 하나의 ReadStatus */
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    /** 마지막 읽음 시간 갱신 */
    void updateLastReadAt(UUID userId, UUID channelId, Instant lastReadAt);

    void deleteById(UUID id);
}