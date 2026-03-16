package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.service.dto.response.ChannelDto;

public record ChannelCreatedEvent(
        ChannelDto channelDto
) {
}