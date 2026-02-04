package com.sprint.mission.discodeit.global.config.security.config;

import com.sprint.mission.discodeit.global.config.security.DiscodeitUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SessionManager {
    private final SessionRegistry sessionRegistry;

    public List<SessionInformation> getAllUserSessions(UUID userId) {
        return sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof DiscodeitUserDetails userDetails) // UserDetails 구현체 필터링
                .map(DiscodeitUserDetails.class::cast) // 형 변환
                .filter(userDetails -> userDetails.getUserResponseDto().id().equals(userId)) // userId로 필터링
                .flatMap(userDetails -> sessionRegistry.getAllSessions(userDetails, false).stream()) // 특정 사용자의 모든 세션들을 가져옴
                .toList();
    }

    public void expireSessionByUserId(UUID userId) {
        List<SessionInformation> userSessionList = getAllUserSessions(userId);
        userSessionList.forEach(SessionInformation::expireNow);
        log.info("{}의 {} 세션 무효화", userId, userSessionList.size());
    }

    public boolean isOnlineByUserId(UUID userId) {
        return !getAllUserSessions(userId).isEmpty();
    }


}
