package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    com.sprint.mission.discodeit.entity.Channel create(
            com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest request
    );

    com.sprint.mission.discodeit.entity.Channel create(
            com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest request
    );

    ChannelDto find(UUID channelId);
    List<ChannelDto> findAllByUserId(UUID userId);
    com.sprint.mission.discodeit.entity.Channel update(
            java.util.UUID channelId,
            com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest request
    );
    void delete(UUID channelId);

}