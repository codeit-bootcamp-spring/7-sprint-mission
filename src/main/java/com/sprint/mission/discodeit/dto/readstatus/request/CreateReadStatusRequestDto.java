package com.sprint.mission.discodeit.dto.readstatus.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateReadStatusRequestDto {
    private UUID userId;
    private UUID channelId;
}
