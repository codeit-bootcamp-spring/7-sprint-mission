package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.repository.BaseRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends BaseRepository<UserStatus> {

    Optional<UserStatus> findByUserId(UUID userId);
}
