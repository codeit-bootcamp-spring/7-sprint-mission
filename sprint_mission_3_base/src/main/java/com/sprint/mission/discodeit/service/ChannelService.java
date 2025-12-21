package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    ChannelDto create(ChannelCreateRequest request);
    ChannelDto update(UUID channelId, ChannelUpdateRequest request);
    void delete(UUID id);
    ChannelDto find(UUID id);
    List<ChannelDto> findAll();

    default List<ChannelDto> findByUserId(UUID userId) {
        return findAll();
    }
}
