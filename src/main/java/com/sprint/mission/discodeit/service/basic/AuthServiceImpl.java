package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.JwtRegistry;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRegistry jwtRegistry;

    @Override
    public boolean isOnline(UUID userId) {
        return jwtRegistry.hasActiveJwtInformationByUserId(userId);
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

    @Override
    public JwtDto rotateRefreshToken(UserDto userDto, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.createAccessToken(userDto);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDto);

        // "REFRESH_TOKEN"이라는 이름의 쿠키 생성
        Cookie refreshCookie = new Cookie("REFRESH_TOKEN", refreshToken);
        refreshCookie.setHttpOnly(true);  // 자바스크립트로 접근 불가 (XSS 방어)
        refreshCookie.setSecure(false);    // HTTPS 환경에서만 전송 (로컬 개발시는 false로 테스트 가능)
        refreshCookie.setPath("/");       // 모든 경로에서 쿠키 전송
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7일 (초 단위) - 리프레시 토큰 유효기간과 맞춤

        // 응답에 쿠키 추가
        response.addCookie(refreshCookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        return new JwtDto(userDto, accessToken, refreshToken);
    }
}
