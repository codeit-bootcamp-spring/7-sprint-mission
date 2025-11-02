package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends BaseRepository<UserStatus> {

    // 유저 아이디로 상태찾기
    Optional<UserStatus> findStatusByUserId(UUID userId);

    void deleteStatusByUserId(UUID userId);
}
