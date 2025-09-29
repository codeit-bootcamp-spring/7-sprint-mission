package com.sprint.mission.discodeit.entity.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface ChannelService {
    void  create(User user,String channelName);
    void read(Channel channel);
    void readAll();
    void update(Channel channel,String channelName);
    void delete(Channel channel);
}
