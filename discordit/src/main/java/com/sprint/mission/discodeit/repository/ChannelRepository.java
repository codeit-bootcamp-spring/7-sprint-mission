package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);
    void update(Channel channel);
    Channel findById(UUID uuid);
    List<Channel> findAll();
    List<Channel> findAllPublic();
    List<Channel> findAllPrivateByUser(User user);
    void deleteById(UUID uuid);
    boolean existsById(UUID uuid);
    boolean existsByName(String name);
}
