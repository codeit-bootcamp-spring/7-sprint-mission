package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.base.UserStatus;

public interface UserStatusRepository {
    void save(UserStatus userStatus);
    UserStatus find(User user);
    void delete(UserStatus userStatus);
}
