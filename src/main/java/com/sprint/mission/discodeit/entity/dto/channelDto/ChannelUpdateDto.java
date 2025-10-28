package com.sprint.mission.discodeit.entity.dto.channelDto;

import java.util.UUID;

public record ChannelUpdateDto(UUID channelId, String newChannelName) {
}
