package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);
    List<Channel> findAll();
    List<Channel> findAllByUserId(UUID userId);
    Optional<Channel> findById(UUID id);
    Optional<Channel> findByName(String name);
    void update(UUID id, String name, String description);
    void delete(UUID id);
    boolean isMember(UUID userId, UUID channelId);
    boolean existsById(UUID id);
}
