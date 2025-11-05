package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.*;

@Getter
@Builder
@ToString
@Setter
public class UserStatus extends Entity{

    private UUID userId;
    private Instant lastOnlineTime;

    public boolean isUserOnline(){
        Duration duration = Duration.between(this.lastOnlineTime, now());
        return duration.getSeconds()<300;
    }
}
