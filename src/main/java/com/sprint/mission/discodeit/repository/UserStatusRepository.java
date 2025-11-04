package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    UserStatus save(UserStatus status);

    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);

    /** 마지막 접속 시간 갱신 */
    void updateLastSeenAt(UUID userId, Instant lastSeenAt);

    void deleteById(UUID id);
}