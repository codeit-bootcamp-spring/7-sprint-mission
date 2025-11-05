package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Builder
public record ChannelRequestDto(@NonNull
                                UUID adminId,
                                @NonNull
                                ChannelType channelType,
                                String channelName,
                                @NonNull
                                List<UUID> memberIds) {
}
