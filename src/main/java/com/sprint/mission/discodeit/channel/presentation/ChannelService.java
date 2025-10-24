package com.sprint.mission.discodeit.channel.presentation;


import com.sprint.mission.discodeit.channel.domain.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void save(Channel channel);

    void remove(Channel channel);

    Channel findById(UUID id);

    List<Channel> findAll();

}
