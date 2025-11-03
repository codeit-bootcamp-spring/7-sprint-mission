package com.sprint.mission.discodeit.dto.readstatus.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateReadStatusRequestDto {
    private UUID userId;
    private UUID channelId;
}
