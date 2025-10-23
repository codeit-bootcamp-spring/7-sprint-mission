package com.sprint.mission.discodeit.userstatus.domain;

import lombok.Getter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

//    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
@Getter
public class UserStatus {

    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;
    private Instant updatedAt;

    public static UserStatus create(UUID userId){
        return new UserStatus(userId);
    }


    public boolean isCurrentlyOnline(Instant lastLoginAt){
        if (lastLoginAt == null) return false;

        Instant now = Instant.now();
        Duration durationSinceLastLogin = Duration.between(lastLoginAt, now);

        // 5분 이내면 현재 접속 중으로 간주
        return durationSinceLastLogin.toMinutes() <= 5;
    }



    private UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId=id;
        this.createdAt=Instant.now();
        this.updatedAt=Instant.now();
    }




}
