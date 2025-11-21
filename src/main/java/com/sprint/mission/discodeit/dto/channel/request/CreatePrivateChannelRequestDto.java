package com.sprint.mission.discodeit.dto.channel.request;

import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelRequestDto(
    List<UUID> participantIds
) { }
