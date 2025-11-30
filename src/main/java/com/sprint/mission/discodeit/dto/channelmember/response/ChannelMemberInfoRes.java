package com.sprint.mission.discodeit.dto.channelmember.response;

import java.util.UUID;

public record ChannelMemberInfoRes(
    String createdAt,
    String updatedAt,
    UUID userId,
    UUID channelId
) {

}
