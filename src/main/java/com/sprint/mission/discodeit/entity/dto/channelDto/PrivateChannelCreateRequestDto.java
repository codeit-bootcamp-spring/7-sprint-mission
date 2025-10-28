package com.sprint.mission.discodeit.entity.dto.channelDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Builder
public record PrivateChannelCreateRequestDto(@NonNull UUID adminId, @NonNull List<UUID> memberIds) {
}
