package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.ChannelDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.MessageRoom;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService extends BaseService<Channel>{
    void update(UUID id, ChannelDTO channelDTO);

    void addMember(Channel channel, UUID uuid);

    void addMessageRoom(Channel channel, UUID uuid);

    List<UUID> getChannelMessageRoomsId(Channel channel);

}
