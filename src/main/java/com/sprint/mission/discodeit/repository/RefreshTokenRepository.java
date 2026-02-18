package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.RefreshToken;
import org.apache.catalina.mapper.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    void deleteByUserId(UUID userId);

    void deleteByToken(String token);

    Optional<RefreshToken> findByUserId(UUID userId);
}
