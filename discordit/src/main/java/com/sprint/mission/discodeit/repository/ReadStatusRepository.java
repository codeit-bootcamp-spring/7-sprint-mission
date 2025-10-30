package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    ReadStatus find(User user, Channel channel);
    ReadStatus findById(UUID uuid);

    void delete(ReadStatus readStatus);
    void deleteAllByUser(User user);
    void deleteAllByChannel(Channel channel);
    void deleteById(UUID uuid);

    List<ReadStatus> findAllByUser(User user);
}
