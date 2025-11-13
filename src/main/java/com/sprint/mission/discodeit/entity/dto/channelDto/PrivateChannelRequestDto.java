package com.sprint.mission.discodeit.entity.dto.channelDto;

import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PrivateChannelRequestDto {

    private List<UUID> participantIds;
}
