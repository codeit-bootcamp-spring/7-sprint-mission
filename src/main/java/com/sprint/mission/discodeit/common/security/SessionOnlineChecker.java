package com.sprint.mission.discodeit.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class SessionOnlineChecker {
    private final SessionRegistry sessionRegistry;

    public boolean isOnline(UUID userId) {
        if (userId == null) {
            return false;
        }

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (!(principal instanceof DiscodeitUserDetails dud)) {
                continue;
            }

            if (dud.getUserDto() == null || dud.getUserDto().id() == null) {
                continue;
            }

            if (dud.getUserDto().id().equals(userId)) {
                return !sessionRegistry.getAllSessions(principal, false).isEmpty();
            }
        }
        return false;
    }

    public Map<UUID, Boolean> onlineMap(Set<UUID> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<UUID, Boolean> result = new HashMap<>();
        for (UUID id : userIds) {
            result.put(id, false);
        }

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (!(principal instanceof DiscodeitUserDetails dud)) {
                continue;
            }
            if (dud.getUserDto() == null || dud.getUserDto().id() == null) {
                continue;
            }

            UUID uuid = dud.getUserDto().id();
            if (!result.containsKey(uuid)) {
                continue;
            }

            List<SessionInformation> alive = sessionRegistry.getAllSessions(principal, false);
            if (!alive.isEmpty()) {
                result.put(uuid, true);
            }
        }
        return result;
    }

    public void expireSession(UUID userId) {
        if (userId == null) {
            return;
        }
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (!(principal instanceof DiscodeitUserDetails dud)) {
                continue;
            }
            if (dud.getUserDto() == null || dud.getUserDto().id() == null) {
                continue;
            }
            if (!dud.getUserDto().id().equals(userId) ) {
                continue;
            }

            for (SessionInformation si : sessionRegistry.getAllSessions(principal, false)) {
                si.expireNow();
                sessionRegistry.removeSessionInformation(si.getSessionId());
            }
        }
    }
}
