package com.sprint.mission.discodeit.domain.user;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

//    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
@Getter
public class UserStatus implements Serializable {



    private final Instant createdAt;
    private final Instant lastLoginAt;
    private final OnlineStatus onlineStatus;


    public UserStatus(OnlineStatus status) {
        this.createdAt=Instant.now();
        this.lastLoginAt=Instant.now();
        this.onlineStatus = status;
    }
}
