package com.sprint.mission.discodeit.service;

import java.util.Set;
import java.util.UUID;

public interface AuthService {

    boolean isOnline(UUID userId);

    void expireUserSession(UUID userId);

    Set<UUID> getOnlineUserIds();
}
