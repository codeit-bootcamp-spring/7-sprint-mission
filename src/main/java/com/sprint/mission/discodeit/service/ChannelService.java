package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface ChannelService {

    Channel create(UUID bose,String channelName);
    Channel find(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channel,String newChannelName, UUID newBose, List<UUID> newUsers);
    void delete(UUID channelId);


}