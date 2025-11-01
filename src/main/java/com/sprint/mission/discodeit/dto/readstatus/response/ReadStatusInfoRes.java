package com.sprint.mission.discodeit.dto.readstatus.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadStatusInfoRes(
        String CreatedAt,
        String UpdatedAt,
        UUID userId,
        UUID channelId
){
    public static ReadStatusInfoRes from(ReadStatus readStatus) {
        return ReadStatusInfoRes.builder()
                .CreatedAt(DateTimeUtil.format(readStatus.getCreatedAt()))
                .UpdatedAt(DateTimeUtil.format(readStatus.getUpdatedAt()))
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .build();
    }
}
