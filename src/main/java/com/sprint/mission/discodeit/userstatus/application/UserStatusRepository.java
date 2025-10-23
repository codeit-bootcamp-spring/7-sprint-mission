package com.sprint.mission.discodeit.userstatus.application;



import com.sprint.mission.discodeit.userstatus.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus userStatus);

    void remove(UserStatus userStatus);

    Optional<UserStatus> findById(UUID id);

    Optional<UserStatus> findByUserId(UUID userId);

    List<UserStatus> findAll();

}
