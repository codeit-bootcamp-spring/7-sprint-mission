package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.domain.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService{

    void save(Channel Channel);

    void remove(Channel Channel);

    Channel findById(UUID id);

    List<Channel> findAll();

    void update(UUID id, ChannelDto channelDTO);

//    void addMember(Channel channel, UUID uuid);
//
//    void addMessageRoom(Channel channel, UUID uuid);
//
//    List<UUID> getChannelMessageRoomsId(Channel channel);

}
