package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final SessionRegistry sessionRegistry;

    @Override
    public boolean isOnline(UUID userId) {
        return getOnlineUserIds().contains(userId);
    }

    @Override
    public void expireUserSession(UUID userId) {
        List<Object> principals = sessionRegistry.getAllPrincipals();

        principals.stream()
                .filter(principal -> principal instanceof DiscodeitUserDetails)
                .map(principal -> ((DiscodeitUserDetails)principal))
                .filter(userDetails -> userDetails.getUserDto().id().equals(userId))
                .findFirst()
                .ifPresentOrElse(principal -> {
                            List<SessionInformation> sessions
                                    = sessionRegistry.getAllSessions(principal, false);
                            log.info("만료할 세션 수: {}", sessions.size());
                            sessions.forEach(session -> {
                                log.info("세션 만료: {}", session.getSessionId());
                                session.expireNow();
                            });
                        },
                        () -> log.warn("사용자를 찾을 수 없음: {}", userId)
                );
    }

    @Override
    public Set<UUID> getOnlineUserIds() {
        return sessionRegistry.getAllPrincipals() .stream()
                .filter(principal -> principal instanceof DiscodeitUserDetails)
                .map(principle -> ((DiscodeitUserDetails)principle).getUserDto().id())
                .collect(Collectors.toSet());
    }
}
