package com.sprint.mission.discodeit.dto.readstatus.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateReadStatusRequestDto {
    private UUID userId;
    private UUID channelId;
}
