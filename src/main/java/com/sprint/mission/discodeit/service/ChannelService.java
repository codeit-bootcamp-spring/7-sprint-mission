package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    Channel createChannel(String channelName);
    Channel findByChannelName(String channelName);
    List<Channel> findAllChannels();
    Channel updateChannel(UUID id, String channelName);
    Channel addMember(UUID channelId, UUID userId);
    Channel removeMember(UUID channelId, UUID userId);
    void deleteChannel(UUID id);
}
