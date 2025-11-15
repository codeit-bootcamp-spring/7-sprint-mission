package com.sprint.mission.discodeit.dto.readstatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.UUID;

public record ReadStatusInfoRes(
    String createdAt,
    String updatedAt,
    UUID userId,
    UUID channelId
) {

  public static ReadStatusInfoRes from(ReadStatus readStatus) {
    return new ReadStatusInfoRes(
        DateTimeUtil.format(readStatus.getCreatedAt()),
        DateTimeUtil.format(readStatus.getUpdatedAt()),
        readStatus.getUserId(),
        readStatus.getChannelId()
    );
  }
}
