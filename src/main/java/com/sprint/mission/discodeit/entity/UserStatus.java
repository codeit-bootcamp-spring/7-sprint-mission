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
    public UserStatus updateOnlineAt(){
        this.onlineAt = Instant.now();
        return this;
    }

    //종료되었을 때
    public UserStatus updateOfflineAt(){
        this.offlineAt = Instant.now();
        return this;
    }

    //온라인 상태 계산(근데 이건 프론트에서 하는게 맞지 않나...?)
    public boolean IsOnline(){
        return onlineAt.isAfter(offlineAt);
    }
}
