package com.sprint.mission.discodeit.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;


@Setter
@Getter
public class ReadStatusDto {

    private UUID id;
    private UUID userId;
    private UUID channelId;
    private Instant lastReadAt;

}
