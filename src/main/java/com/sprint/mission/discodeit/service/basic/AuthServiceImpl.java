package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.jwt.JwtDto;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.JwtInformation;
import com.sprint.mission.discodeit.security.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.JwtRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
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
    @CacheEvict(value = "allUsers", allEntries = true)
    public void expireUserSession(UUID userId) {
        jwtRegistry.invalidateJwtInformationByUserId(userId);
        log.info("JWT 무효화: userId={}", userId);
    }

    @Override
    public Set<UUID> getOnlineUserIds() {
        return jwtRegistry.getAllActiveUserIds();
    }

    @Override
    @CacheEvict(value = "allUsers", allEntries = true)
    public JwtDto rotateRefreshToken(UserDto userDto) {
        String accessToken = jwtTokenProvider.createAccessToken(userDto);
        String refreshToken = jwtTokenProvider.createRefreshToken(userDto);

        JwtInformation newJwtInfo = new JwtInformation(userDto, accessToken, refreshToken);
        jwtRegistry.registerJwtInformation(newJwtInfo);


        return new JwtDto(userDto, accessToken, refreshToken);
    }
}
