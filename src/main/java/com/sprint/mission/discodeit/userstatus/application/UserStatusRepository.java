package com.sprint.mission.discodeit.userstatus.application;



import com.sprint.mission.discodeit.userstatus.domain.UserStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserStatusRepository {
    void save(UserStatus user);

    void remove(UserStatus user);

    Optional<UserStatus> findById(UUID id);

    List<UserStatus> findAll();

}
