package com.sprint.mission.discodeit.dto.request.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class PrivateChannelCreateRequestDto {
    private final List<UUID> userIds;
    private final Integer slowModeSeconds;
    private final ChannelType channelType;
}
