package com.sprint.mission.discodeit.domain.repository;

import com.sprint.mission.discodeit.domain.Channel;

import java.util.List;
import java.util.Optional;


public interface ChannelRepository {
    void save(Channel channel);

    void delete(Channel channel);

    Optional<Channel> findById(String id);

    List<Channel> findAll();
}
