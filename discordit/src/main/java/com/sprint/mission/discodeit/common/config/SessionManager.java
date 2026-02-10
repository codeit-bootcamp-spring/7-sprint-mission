package com.sprint.mission.discodeit.common.config;

import com.sprint.mission.discodeit.dto.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionManager {
    private final SessionRegistry sessionRegistry;

    public void invalidateUserSessions(String username) {
        sessionRegistry.getAllPrincipals().stream()
                .filter(p -> p instanceof DiscodeitUserDetails)
                .filter(p -> ((DiscodeitUserDetails) p).getUserDto().username().equals(username))
                .forEach(p ->
                        sessionRegistry.getAllSessions(p, false)
                                .forEach(SessionInformation::expireNow)
                );
    }

    public boolean isOnline(String userName) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(p -> p instanceof DiscodeitUserDetails)
                .map(p -> (DiscodeitUserDetails) p)
                .filter(p -> p.getUsername().equals(userName))
                .flatMap(p ->
                        sessionRegistry.getAllSessions(p, false).stream())
                .anyMatch(session -> !session.isExpired());

    }
}
