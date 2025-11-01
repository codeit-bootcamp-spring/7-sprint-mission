package com.sprint.mission.discodeit.dto.readstatus.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadStatusCreateReq(
        UUID userId,
        UUID channelId
) {}
