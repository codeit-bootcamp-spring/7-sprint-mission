package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusService {

    void update(UUID userId);

    Optional<UserStatus> findByUserId(UUID userId);
}
