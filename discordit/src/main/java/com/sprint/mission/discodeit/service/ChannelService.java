package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelMemberRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.UserDto;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto createPublicChannel(PublicChannelCreateRequest dto);

    ChannelDto createPrivateChannel(PrivateChannelCreateRequest dto);

    ChannelDto update(UUID id, ChannelUpdateRequest dto);

    void addParticipant(ChannelMemberRequest dto);

    void removeParticipant(ChannelMemberRequest dto);

    ChannelDto get(UUID id);

    List<ChannelDto> getAll();

    List<UserDto> getAllParticipants(UUID id);

    List<ChannelDto> getAllVisibleByUser(UUID userId);

    void delete(UUID id);
}
