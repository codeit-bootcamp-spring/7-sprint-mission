package com.sprint.mission.discodeit.entity.dto.channelDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Builder
public record PrivateChannelRequestDto(@NonNull UUID adminId, @NonNull List<UUID> memberIds) {
}
