package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Builder
@ToString
@Getter
@Setter
public class ReadStatus extends Entity {

    private UUID userId;
    private UUID channelId;
    private Instant readLastTime;

}
