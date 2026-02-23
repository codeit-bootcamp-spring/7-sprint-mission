package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.security.SessionOnlineChecker;
import com.sprint.mission.discodeit.common.security.jwt.*;
import com.sprint.mission.discodeit.dto.response.auth.AuthTokenResponseDto;
import com.sprint.mission.discodeit.dto.response.auth.JwtDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicAuthService implements AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final SessionOnlineChecker sessionOnlineChecker;
    private final UserMapper userMapper;
    private final RefreshTokenCookieManager refreshTokenCookieManager;
    private final JwtRegistry jwtRegistry;
    private final JwtOnlineChecker jwtOnlineChecker;

    @Override
    public JwtDto refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null || refreshToken.isBlank()) {
            refreshTokenCookieManager.clear(response);
            throw new InsufficientAuthenticationException("Refresh token is empty");
        }

        if (!jwtRegistry.hasActiveJwtInformationByRefreshToken(refreshToken)) {
            refreshTokenCookieManager.clear(response);
            throw new InsufficientAuthenticationException("Refresh token is not active");
        }

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            refreshTokenCookieManager.clear(response);
            throw new InsufficientAuthenticationException("Invalid refresh token");
        }

        UUID userId;

        try {
            userId = UUID.fromString(jwtTokenProvider.getSubject(refreshToken));
        } catch (Exception e) {
            refreshTokenCookieManager.clear(response);
            throw new InsufficientAuthenticationException("Invalid refresh token subject");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    refreshTokenCookieManager.clear(response);
                    throw new InsufficientAuthenticationException("user not found");
                });

        String role = user.getRole().name();
        AuthTokenResponseDto pair = jwtTokenProvider.issueTokenPair(user.getId(), role);


        boolean online = jwtOnlineChecker.isOnline(userId);
        UserResponseDto userDto = userMapper.toDto(user, online);

        JwtInformation newInfo = new JwtInformation(
                userDto,
                pair.accessToken(),
                jwtTokenProvider.getExpirationInstant(pair.accessToken()),
                pair.refreshToken(),
                jwtTokenProvider.getExpirationInstant(pair.refreshToken())
        );

        boolean rotated = jwtRegistry.rotateJwtInformation(refreshToken, newInfo);
        if (!rotated) {
            refreshTokenCookieManager.clear(response);
            throw new InsufficientAuthenticationException("Refresh token is not active");
        }

        refreshTokenCookieManager.set(response, pair.refreshToken());


        return new JwtDto(userDto, pair.accessToken());
    }
}
