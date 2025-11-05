package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class ReadStatusResponseDto {
    private UUID userId;
    private UUID channelId;
    private Instant readLastTime;

    public static ReadStatusResponseDto from(ReadStatus readStatus){
        return ReadStatusResponseDto.builder()
                .userId(readStatus.getUserId())
                .channelId(readStatus.getChannelId())
                .readLastTime(readStatus.getReadLastTime())
                .build();
    }
}
