package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private final UUID userId;      //접속자 UUID
    private Instant offlineAt;      //로그아웃 한 시간
    private Instant onlineAt;       //로그인 한 시간
    private boolean isOnline;
    public static final int OFFLINE_THRESHOLD_SECONDS = 300;       //오프라인 기준 타임

    //Constructor
    public UserStatus(UUID userId) {
        this.userId = userId;
        this.offlineAt = Instant.now();
    }

    //로그인 했을 때
    public UserStatus updateOnlineAt(){
        super.update();
        this.onlineAt = Instant.now();
        return this;
    }

    //종료되었을 때
    public UserStatus updateOfflineAt(){
        super.update();
        this.offlineAt = Instant.now();
        return this;
    }

    //온라인 상태 계산
    public void updateOnline(){
        super.update();
        this.isOnline = offlineAt.isAfter(Instant.now().minusSeconds(OFFLINE_THRESHOLD_SECONDS));
    }
}
