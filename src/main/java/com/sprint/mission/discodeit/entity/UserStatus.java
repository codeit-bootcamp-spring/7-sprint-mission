package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@ToString
@Builder
public class UserStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID userId;      //접속자 UUID
    private Instant offlineAt;      //로그아웃 한 시간
    private Instant onlineAt;       //로그인 한 시간

    //Constructor
    public UserStatus(UUID userId) {
        this.userId = userId;
        this.offlineAt = Instant.now();
    }

    //로그인 했을 때
    public void updateOnlineAt(){
        this.onlineAt = Instant.now();
    }

    //종료되었을 때
    public void updateOfflineAt(){
        this.offlineAt = Instant.now();
    }

    //온라인 상태 계산
    public boolean IsOnline(){
        return onlineAt.isAfter(offlineAt);
    }
}
