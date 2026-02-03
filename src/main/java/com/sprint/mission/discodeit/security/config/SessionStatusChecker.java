package com.sprint.mission.discodeit.security.config;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionStatusChecker {

    private final SessionRegistry sessionRegistry;

    public boolean isOnline(String username) {
        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof DiscodeitUserDetails userDetails) {
                if (username.equals(userDetails.getUsername())) {
                    return !sessionRegistry
                            .getAllSessions(userDetails, false)
                            .isEmpty();
                }
            }
        }
        return false;
    }
}
