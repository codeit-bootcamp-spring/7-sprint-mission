package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public interface ChannelService {
    Channel  create(User user,String channelName);
    Channel read(UUID channelId);
    List<Channel> readAll();
    Channel update(UUID channelId, Consumer<Channel> updater);
    boolean delete(UUID channelId);

}
