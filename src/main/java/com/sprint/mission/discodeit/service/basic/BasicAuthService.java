package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.auth.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.jwt.BusinessException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.security.jwt.JwtDto;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final SessionRegistry sessionRegistry;
    private final DiscodeitUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto updateRole(UserRoleUpdateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        user.updateRole(request.newRole());
        User saved = userRepository.save(user);

        DiscodeitUserDetails userDetails = (DiscodeitUserDetails) userDetailsService.loadUserByUsername(user.getUsername());

        List<SessionInformation> sessions = sessionRegistry.getAllSessions(userDetails, false);

        if (!sessions.isEmpty()) {
            sessions.forEach(SessionInformation::expireNow);
            log.info("사용자 {}의 모든 세션({}개) 무효화 완료", user.getUsername(), sessions.size());
        } else {
            log.info("사용자 {}에 활성 세션 없음", user.getUsername());
        }

        return userMapper.toResponseDto(saved);
    }

    @Transactional
    public JwtDto refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookies(request);

        if (refreshToken == null || !jwtTokenProvider.isTokenValid(refreshToken)) {
            throw new BusinessException(ErrorCode.EMPTY_OR_INVALID_TOKEN);
        }

        Claims claims = jwtTokenProvider.validateToken(refreshToken);
        String username = claims.getSubject();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

        String newAccessToken = jwtTokenProvider.generateAccessToken(user);

        // Refresh Token Rotation
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);

        Cookie refreshTokenCookie = new Cookie("REFRESH_TOKEN", newRefreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(request.isSecure());
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(60 * 60 * 24 * 14);

        response.addCookie(refreshTokenCookie);

        return new JwtDto(userMapper.toResponseDto(user), newAccessToken);
    }

    private String extractRefreshTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> "REFRESH_TOKEN".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
