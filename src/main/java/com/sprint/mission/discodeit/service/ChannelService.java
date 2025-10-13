package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.dto.ChannelInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {

    ChannelInfo create(UUID userId, Channel.ChannelType type);
    ChannelInfo create(UUID userId, String channelName, Channel.ChannelType type);

    Optional<ChannelInfo> findChannelInfoById(UUID id);
    Optional<Channel> findChannelEntityById(UUID id);
    List<ChannelInfo> findAll();

    Optional<ChannelInfo> updateChannelName(UUID channelId, String channelName);
    Optional<ChannelInfo> addMemberToChannel(UUID channelId, UUID userId);
    Optional<ChannelInfo> removeMemberFromChannel(UUID channelId, UUID userId);

    boolean deleteChannel(UUID id);

    int deleteUserFromAllChannel(User user);
}
