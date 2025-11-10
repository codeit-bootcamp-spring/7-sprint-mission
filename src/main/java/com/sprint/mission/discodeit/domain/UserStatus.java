package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

//    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
@Getter
public class UserStatus implements Serializable {

    private final Instant lastAt;
    private final OnlineStatus onlineStatus;


    public UserStatus(OnlineStatus status) {
        this.lastAt=Instant.now();
        this.onlineStatus = status;
    }

    public UserStatus(OnlineStatus status,Instant lastAt) {
        this.lastAt=lastAt;
        this.onlineStatus = status;
    }

    public boolean isOnline(){
        if(onlineStatus.equals(OnlineStatus.ONLINE)){
            return true;
        }
        return false;
    }
}
