package com.sprint.mission.discodeit.dto.readStatus;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadStatusCreateReq(
        UUID userId,
        UUID channelId
) {}
