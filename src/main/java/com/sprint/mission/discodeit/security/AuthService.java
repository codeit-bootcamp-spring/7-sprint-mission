package com.sprint.mission.discodeit.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService{

    private final UserSessionRegistry userSessionRegistry;

    public void invalidateUserSession(UUID userId) {
        userSessionRegistry.expireUserSessions(userId);
    }

    public boolean isUserLoggedIn(UUID userId) {
        return userSessionRegistry.isUserLoggedIn(userId);
    }
}
