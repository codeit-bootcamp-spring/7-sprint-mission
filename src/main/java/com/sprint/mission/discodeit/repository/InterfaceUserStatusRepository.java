package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

public interface InterfaceUserStatusRepository extends BaseInterfaceRepository<UserStatus>{
    boolean isOnline();
}
