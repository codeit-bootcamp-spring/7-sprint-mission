package com.sprint.mission.discodeit.domain.channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel);

    void remove(Channel channel);

    Optional<Channel> findById(UUID id);

    List<Channel> findAll();


}
