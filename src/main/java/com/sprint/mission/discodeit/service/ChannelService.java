package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.*;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    ChannelDto create(PublicChannelCreateRequest request);

    ChannelDto create(PrivateChannelCreateRequest request);

    ChannelDto find(UUID channelId);

    List<ChannelDto> findAllByUserId(UUID userId);

    ChannelDto update(UUID channelId, ChannelUpdateRequest request);

    void delete(UUID channelId);


}