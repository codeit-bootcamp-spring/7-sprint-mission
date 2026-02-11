/*
package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
//    사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델
//    사용자의 온라인 상태를 확인하기 위해 활용
//    마지막 접속 시간이 현재 시간으로부터 5분 이내면 현재 접속 중인 유저!
@Getter
@ToString
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity {
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    protected UserStatus() {}

    public UserStatus(User user) {
        this.user = Objects.requireNonNull(user);
        this.lastActiveAt = Instant.now();
    }

    public void timeUpdated() {
        this.lastActiveAt = Instant.now();
    }

    public boolean isOnlineNow() {
        Instant time = Instant.now().minus(Duration.ofMinutes(5));
        return lastActiveAt.isAfter(time);
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        Objects.requireNonNull(lastActiveAt);
        this.lastActiveAt = lastActiveAt;
    }
}
*/
