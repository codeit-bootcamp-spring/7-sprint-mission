package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.function.Consumer;

public interface ChannelService {

    void createChannel(Channel channel);
    void readChannel(Channel channel);
    void readAllChannel();
    void deleteChannel(Channel channel);
    <T> void updateChannel(Channel channel, Channel.channelElement channelElement, T updatedContent);
    void readUpdatedChannel();
    void readDeletedChannel();
    void inviteUserToChannel(User user, Channel channel);
    void deleteUserFromChannel(User user, Channel channel);
}
