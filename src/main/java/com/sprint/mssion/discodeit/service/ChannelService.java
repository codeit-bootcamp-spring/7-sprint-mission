package com.sprint.mssion.discodeit.service;

import com.sprint.mssion.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    public Channel create(Channel.ChannelType type, String channelName, String channelDescription);
    public Channel read(UUID channelId);
    public List<Channel> readAll();
    public void update(UUID channelId, Channel.ChannelType type, String channelName, String channelDescription);
    public void delete(UUID channelId);
}
