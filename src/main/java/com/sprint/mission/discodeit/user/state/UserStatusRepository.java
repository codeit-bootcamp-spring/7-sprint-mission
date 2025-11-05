package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.common.repository.BaseRepository;
import com.sprint.mission.discodeit.config.enums.Status;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository extends BaseRepository<UserStatus, UUID> {
    Optional<UserStatus> findByUserId(UUID userId);
    Optional<UserStatus> findByUserIdNonDel(UUID userId);

    boolean existsByUserId(UUID userId);
    boolean existsByUserIdNonDel(UUID userId);

    List<UserStatus> findAllByState(Status currentStatus);
}
