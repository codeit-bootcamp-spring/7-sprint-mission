package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    void save(ReadStatus readStatus);

    Optional<ReadStatus> find(User user, Channel channel);
    Optional<ReadStatus> findById(UUID uuid);
    List<ReadStatus> findAll();

    void delete(ReadStatus readStatus);
    void deleteAllByUser(User user);
    void deleteAllByChannel(Channel channel);
    void deleteById(UUID uuid);

    List<ReadStatus> findAllByUser(User user);
}
