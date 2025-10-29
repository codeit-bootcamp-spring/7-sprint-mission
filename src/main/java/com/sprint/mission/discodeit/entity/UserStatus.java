package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
public class UserStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID userId;
    private boolean online;
    private Instant offlineAt;

    //Constructor
    public UserStatus(UUID userId) {
        this.userId = userId;
        this.online = false;
    }

    //온라인 상태 계산
    public void online(){
        Instant baseTime = Instant.now().minusMillis(300);
        this.online = offlineAt.isAfter(baseTime);
    }
}
