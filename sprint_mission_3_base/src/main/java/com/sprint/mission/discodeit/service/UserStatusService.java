package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {
    UUID create(UserStatusCreateRequest request);
    Optional<Instant> findLastSeenByUserId(UUID userId);
    List<UUID> findAllUserIdsOnlineWithinMinutes(int minutes);
    void update(UserStatusUpdateRequest request);
    UserStatusDto updateByUserId(UUID userId, Instant lastSeenAt);
    void delete(UUID id);
}
