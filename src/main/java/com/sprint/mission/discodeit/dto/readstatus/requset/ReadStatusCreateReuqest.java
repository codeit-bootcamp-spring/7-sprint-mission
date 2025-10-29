package com.sprint.mission.discodeit.dto.readstatus.requset;

import java.util.UUID;

public record ReadStatusCreateReuqest(
          UUID userId,
          UUID channelId
) {
}
