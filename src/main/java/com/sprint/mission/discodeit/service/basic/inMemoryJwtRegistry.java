package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.JwtInformation;
import com.sprint.mission.discodeit.service.JwtRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class inMemoryJwtRegistry implements JwtRegistry {

    private final Map<UUID, JwtInformation> registry = new ConcurrentHashMap<>();

    @Override
    public void registerJwtInformation(JwtInformation jwtInformation) {
        registry.put(jwtInformation.getUserDto().id(), jwtInformation);
    }

    @Override
    public void invalidateJwtInformationByUserId(UUID userId) {

    }

    @Override
    public boolean hasActiveJwtInformationByUserId(UUID userId) {
        return false;
    }

    @Override
    public boolean hasActiveJwtInformationByAccessToken(String accessToken) {
        return false;
    }

    @Override
    public boolean hasActiveJwtInformationByRefreshToken(String refreshToken) {
        return false;
    }

    @Override
    public void rotateJwtInformation(String refreshToken, JwtInformation jwtInformation) {

    }

    @Override
    public void clearExpiredJwtInformation() {

    }
}
