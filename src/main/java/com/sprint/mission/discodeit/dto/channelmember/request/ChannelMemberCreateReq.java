package com.sprint.mission.discodeit.dto.channelmember.request;

import com.sprint.mission.discodeit.entity.ChannelMemberRole;
import java.util.UUID;

public record ChannelMemberCreateReq(
    UUID userId,
    UUID channelId,
    ChannelMemberRole role
) {

}
