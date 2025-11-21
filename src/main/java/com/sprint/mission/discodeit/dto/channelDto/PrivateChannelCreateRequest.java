package com.sprint.mission.discodeit.dto.channelDto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PrivateChannelCreateRequest {

    private List<UUID> participantIds;
}
