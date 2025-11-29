package com.sprint.mission.discodeit.dto.channelmember.response;

import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.UUID;

public record ReadStatusInfoRes(
    String createdAt,
    String updatedAt,
    UUID userId,
    UUID channelId
) {

  public static ReadStatusInfoRes from(ChannelMember channelMember) {
    return new ReadStatusInfoRes(
        DateTimeUtil.format(channelMember.getCreatedAt()),
        DateTimeUtil.format(channelMember.getUpdatedAt()),
        channelMember.getUserId(),
        channelMember.getChannelId()
    );
  }
}
