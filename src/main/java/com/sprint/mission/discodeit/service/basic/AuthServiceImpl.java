package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.security.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.JwtRegistry;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRegistry jwtRegistry;

    @Override
    public boolean isOnline(UUID userId) {
        return jwtRegistry.hasActiveJwtInformationByUserId(userId);
    }

    @Override
    public void expireUserSession(UUID userId) {
        jwtRegistry.invalidateJwtInformationByUserId(userId);
        log.info("JWT 무효화: userId={}", userId);
    }

    @Override
    public Set<UUID> getOnlineUserIds() {
        return jwtRegistry.getAllActiveUserIds();
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
