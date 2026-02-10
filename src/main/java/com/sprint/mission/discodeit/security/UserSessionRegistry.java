package com.sprint.mission.discodeit.security;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//@Component
public class UserSessionRegistry {

    private final Map<UUID, Object> userIndex = new ConcurrentHashMap<>();
    private final SessionRegistry sessionRegistry;

    public UserSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    // 1. 세션이 생성/로그인될 때 발생하는 이벤트를 가로챔
    @EventListener
    public void onSessionCreated(InteractiveAuthenticationSuccessEvent event) {
        Object principal = event.getAuthentication().getPrincipal();
        if (principal instanceof DiscodeitUserDetails userDetails) {
            // 접속 시점에 ID와 객체를 매핑해서 저장 (O(1) 조회를 위해)
            userIndex.put(userDetails.getUserDto().getId(), principal);
        }
    }

    @EventListener
    public void onSessionExpired(SessionDestroyedEvent event) {
        // SecurityContext에서 인증 정보를 꺼냄
        List<SecurityContext> contexts = event.getSecurityContexts();
        for (SecurityContext context : contexts) {
            Object principal = context.getAuthentication().getPrincipal();
            if (principal instanceof DiscodeitUserDetails userDetails) {
                // 장부에서 삭제
                userIndex.remove(userDetails.getUserDto().getId());
            }
        }
    }


    public void expireUserSessions(UUID userId) {
        Object principal = userIndex.get(userId);
        if (principal != null) {
            List<SessionInformation> sessions = sessionRegistry.getAllSessions(principal, false);
            for (SessionInformation session : sessions) {
                session.expireNow();
            }
            userIndex.remove(userId); // 장부에서 제거
        }
    }

    public boolean isUserLoggedIn(UUID userId) {
        return userIndex.containsKey(userId);
    }
}