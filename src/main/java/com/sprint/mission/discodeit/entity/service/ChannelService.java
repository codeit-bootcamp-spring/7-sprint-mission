package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void  create(User user,String channelName);
    void read(UUID channelId);
    void readAll();
    void update(UUID channelId,String channelName);
    void delete(UUID channelId);
}
