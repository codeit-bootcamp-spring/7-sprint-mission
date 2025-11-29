package com.sprint.mission.discodeit.dto.channelmember.response;

import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.UUID;

public record ChannelMemberInfoRes(
    String createdAt,
    String updatedAt,
    UUID userId,
    UUID channelId
) {

  public static ChannelMemberInfoRes from(ChannelMember channelMember) {
    return new ChannelMemberInfoRes(
        DateTimeUtil.format(channelMember.getCreatedAt()),
        DateTimeUtil.format(channelMember.getUpdatedAt()),
        channelMember.getUser().getId(),
        channelMember.getChannel().getId()
    );
  }
}
