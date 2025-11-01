package com.sprint.mission.discodeit.dto.readstatus;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadStatusCreateReq(
        UUID userId,
        UUID channelId
) {}
