package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.update.ChannelUpdate;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel create(Channel channel);

    Channel read(UUID id);

    List<Channel> readAll();

    Channel update(UUID id, ChannelUpdate chu);

    void delete(UUID id);
}
