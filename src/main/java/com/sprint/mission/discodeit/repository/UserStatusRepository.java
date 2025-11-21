package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {

    // 유저 아이디로 상태찾기
    Optional<UserStatus> findByUserId(UUID userId);
    void deleteByUserId(UUID userId);
}
