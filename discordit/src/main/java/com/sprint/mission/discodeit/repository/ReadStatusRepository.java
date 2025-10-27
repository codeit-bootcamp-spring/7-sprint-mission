package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.base.Channel;
import com.sprint.mission.discodeit.entity.base.ReadStatus;
import com.sprint.mission.discodeit.entity.base.User;
import com.sprint.mission.discodeit.entity.base.UserStatus;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);
    ReadStatus find(User user, Channel channel);
    void delete(ReadStatus readStatus);
    void deleteAllByUser(User user);

}
