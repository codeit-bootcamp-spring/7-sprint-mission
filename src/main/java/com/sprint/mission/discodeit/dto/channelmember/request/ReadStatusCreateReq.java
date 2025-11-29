package com.sprint.mission.discodeit.dto.channelmember.request;

import java.util.UUID;

public record ReadStatusCreateReq(
    UUID userId,
    UUID channelId
) {

}
