package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel.ChannelType channelType, String channelName, User admin);
    void addMember(UUID id, User member);

    Channel getChannel(UUID id);
    List<Channel> getChannelByUser(User user);
    List<Channel> getChannelByType(Channel.ChannelType channelType);
    List<Channel> getAllChannels();

    void updateAdmin(UUID id, User user);
    void updateName(UUID id, String name);

    void delChannel(UUID id, User user);
    void delChannelMember(UUID id, User requester, User target);
}
