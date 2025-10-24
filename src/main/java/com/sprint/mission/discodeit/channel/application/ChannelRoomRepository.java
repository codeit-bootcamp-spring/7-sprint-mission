package com.sprint.mission.discodeit.channel.application;

import com.sprint.mission.discodeit.channel.domain.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRoomRepository {
    void save(Channel channel);

    void remove(Channel channel);

    Optional<Channel> findById(UUID id);

    List<Channel> findAll();
}
