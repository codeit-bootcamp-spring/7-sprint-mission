package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;

import java.util.UUID;
import java.util.List;

public interface ChannelService {

    ChannelDto create(ChannelCreateRequest request);

    ChannelDto update(ChannelUpdateRequest request);

    void delete(UUID id);        // <-- String → UUID 로 변경

    ChannelDto find(UUID id);    // <-- String → UUID 로 변경

    List<ChannelDto> findAll();
}
