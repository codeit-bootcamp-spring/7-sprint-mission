package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.UUID;

public class JCF_ChannelRepository implements ChannelRepository {
    @Override
    public Channel createChannel(User ownerUser, String channelName) {
        return null;
    }

    @Override
    public void updateChannelName(Channel channel, String name) {

    }

    @Override
    public void updateChannelType(Channel channel, ChannelType channelType) {

    }

    @Override
    public void deleteChannel(UUID uuid) {

    }
}
