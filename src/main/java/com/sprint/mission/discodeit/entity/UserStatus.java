package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdatableEntity{
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "last_active_at", nullable = false)
    private Instant lastActiveAt;

    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public void update(Instant lastActiveAt){
        if(lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)){
            this.lastActiveAt = lastActiveAt;
        }
    }

    public boolean isOnline() {
        Instant lastLogin = lastActiveAt;
        Instant now = Instant.now();
        Duration duration = Duration.between(lastLogin, now);

        if (duration.toMinutes() < 5) return true; //접속이 5분이내이면 현재 접속중인 유저로 간주
        else return false;
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "UserStatus{" +
                "user=" + user +
                ", lastActiveAt=" + lastActiveAt +
                str +
                '}';
    }
}
