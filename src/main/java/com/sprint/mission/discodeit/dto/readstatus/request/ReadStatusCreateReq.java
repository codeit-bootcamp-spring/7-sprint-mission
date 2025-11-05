package com.sprint.mission.discodeit.dto.readstatus.request;

import java.util.UUID;

public record ReadStatusCreateReq(
        UUID userId,
        UUID channelId
) {}
