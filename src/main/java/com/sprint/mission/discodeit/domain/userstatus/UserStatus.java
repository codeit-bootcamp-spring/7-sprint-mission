package com.sprint.mission.discodeit.domain.userstatus;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

//    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
@Getter
public class UserStatus implements Serializable {

    private static final long serialVersionUID = 6L;

    private final UUID id;
    private final UUID userId;
    private final Instant createdAt;
    private Instant lastLoginAt;
    private OnlineStatus status;

    public static UserStatus create(UUID userId){
        return new UserStatus(userId);
    }

    private UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.userId=id;
        this.createdAt=Instant.now();
        this.lastLoginAt=Instant.now();
        this.status=OnlineStatus.OFFLINE;
    }

    //근데 이건 누가 사용하는 메서드임 대체? 친구들?
    public boolean isCurrentlyOnline(){

        Instant now = Instant.now();
        Duration durationSinceLastLogin = Duration.between(lastLoginAt, now);

        // 로그아웃이 5분 이내면 현재 접속 중으로 간주
        return durationSinceLastLogin.toMinutes() <= 5;
    }

    // ✅ 로그인 시 호출
    public void markOnline() {
        this.status = OnlineStatus.ONLINE;
        this.lastLoginAt = Instant.now();
    }

    // ✅ 로그아웃 시 호출
    public void markOffline() {
        this.status = OnlineStatus.OFFLINE;
        this.lastLoginAt = Instant.now();
    }







}
