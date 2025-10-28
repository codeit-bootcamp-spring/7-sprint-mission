package com.sprint.mission.discodeit.entity.dto.channelDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record PublicChannelCreateRequestDto(@NonNull UUID adminId, String channelName) {
}
