package com.sprint.mission.discodeit.dto.channelmember.request;

import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import java.util.UUID;

public record ReadStatusCreateReq(
    UUID userId,
    UUID channelId,
    ChannelMemberRole role
) {

}
