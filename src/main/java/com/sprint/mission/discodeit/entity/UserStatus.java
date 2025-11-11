package com.sprint.mission.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseEntity{
    private static final long serialVersionUID = 1L;

    //Field
    private UUID userId;      //접속자 UUID
    private Instant offlineAt;      //로그아웃 한 시간
    private Instant onlineAt;       //로그인 한 시간
    private boolean isOnline;
    public static final int OFFLINE_THRESHOLD_SECONDS = 60;       //오프라인 기준 타임

    //Constructor
    private UserStatus(UUID userId) {
        this.userId = userId;
        this.offlineAt = Instant.now();
    }

    //Factory Method
    public static UserStatus create(UUID userId){
        return new UserStatus(userId);
    }

    //로그인 했을 때
    public void updateOnlineAt(){
        super.update();
        this.onlineAt = Instant.now();
    }

    //종료되었을 때
    public void updateOfflineAt(){
        super.update();
        this.offlineAt = Instant.now();
    }

    //온라인 상태 계산
    public void updateOnline(){
        super.update();
        this.isOnline = offlineAt.isAfter(Instant.now().minusSeconds(OFFLINE_THRESHOLD_SECONDS));
    }
}
