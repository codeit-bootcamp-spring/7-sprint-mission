package com.sprint.mission.discodeit.entity;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public class UserStatus extends BaseModel {
//    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다. 사용자의 온라인 상태를 확인하기 위해 활용합니다.
    private UUID userId;
//    private static final Duration ONLINE_DURATION = Duration.ofMinutes(5);

    public UserStatus(UUID userId) {
        super();
        this.userId = userId;
    }

    public String toString() {
        return "🌸 super = " + super.toString() + "/ userId = [" + userId + "]";
    }

    public boolean isOnline() {
        //[ ] 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드를 정의하세요.
        // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주합니다.
        Duration duration = Duration.between(this.getUpdatedAt(), Instant.now());
        boolean isOnline = duration.toMinutes() < 5;
//        log.info("✅ ❌ UserStatus.online() = [" + online + "]");
        return isOnline;
    }
}
