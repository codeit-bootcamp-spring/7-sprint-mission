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
    private Instant offlineAt;

    //Constructor
    public UserStatus(UUID userId) {
        this.userId = userId;
    }

    //종료되었을 때
    public void updateOfflineAt(){
        this.offlineAt = Instant.now();
    }

    //온라인 상태 계산
    public boolean IsOnline(){
        Instant baseTime = Instant.now().minusMillis(300);
        return offlineAt.isAfter(baseTime);
    }
}
