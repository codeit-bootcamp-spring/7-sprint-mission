package com.sprint.mission.discodeit.domain;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;


@Getter
public class UserStatus {

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
