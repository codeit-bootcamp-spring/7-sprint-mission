package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelInfo createChannel(UUID userId, ChannelType type);
    ChannelInfo createChannel(UUID userId, String channelName, ChannelType type);

    Optional<ChannelInfo> findChannelInfoById(UUID id);
    Optional<Channel> findChannelEntityById(UUID id);
    List<ChannelInfo> findAll();
    Optional<ChannelInfo> findChannelInfoByChannelName(String channelName);

    Optional<ChannelInfo> updateChannelName(UUID channelId, String channelName);
    Optional<ChannelInfo> addMemberToChannel(UUID channelId, UUID userId);
    Optional<ChannelInfo> removeMemberFromChannel(UUID channelId, UUID userId);

    boolean deleteChannel(UUID id);

}
