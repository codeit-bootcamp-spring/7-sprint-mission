package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public interface UserStatusService {
    UserStatus create(UserStatus userStatus);
    UserStatus findById(UUID id);
    UserStatus findByUserId(UUID userId);
    UserStatus updateOnlineAt(UUID userId);
    UserStatus updateOfflineAt(UUID userId);
    UserStatus delete(UUID id);
}
