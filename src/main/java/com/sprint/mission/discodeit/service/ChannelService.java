package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;
// 네이밍 컨벤션 해야함. createUser, creaChnael 등등,
// 인터페이스에 너무 잡다한게 많은거같다.

public interface ChannelService {
    Channel createChannel(Channel.ChannelType type, String channelName, String channelDescription);
    Channel getChannel(UUID channelId);
    List<Channel> getAllChannels();
    void updateChannel(UUID channelId, Channel.ChannelType type, String channelName, String channelDescription);
    void deleteChannel(UUID channelId);
    boolean isExistsChannel(UUID channelId);

}
