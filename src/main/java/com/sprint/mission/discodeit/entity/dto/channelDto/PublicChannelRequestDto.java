package com.sprint.mission.discodeit.entity.dto.channelDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record PublicChannelRequestDto(@NonNull UUID adminId, String channelName) {
}
