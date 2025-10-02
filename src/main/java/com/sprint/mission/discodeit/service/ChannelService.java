package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.ChannelInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    Channel create(User user, Channel.ChannelType type);
    Channel create(User user, String channelName, Channel.ChannelType type);

    Optional<ChannelInfo> findById(UUID id);
    List<ChannelInfo> findAll();

    Optional<ChannelInfo> updateChannelName(UUID channelId, String channelName);
    Optional<ChannelInfo> addMember(UUID channelId, UUID userId);
    Optional<ChannelInfo> removeMember(UUID channelId, UUID userId);

    boolean deleteChannel(UUID id);
}
