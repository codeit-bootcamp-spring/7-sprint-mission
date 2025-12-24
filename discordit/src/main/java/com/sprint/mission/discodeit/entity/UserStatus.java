package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static com.sprint.mission.discodeit.common.config.OnlineThreshold.ONLINE_THRESHOLD;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_statuses")
@Getter
public class UserStatus extends BaseEntity {

    @OneToOne
    private User user;

    private Instant lastActiveAt;

    public UserStatus(User user) {
        this.user = user;
        lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
        user.setOnline(isOnline());
    }

    public boolean isOnline() {
        return lastActiveAt.isAfter(Instant.now().minus(ONLINE_THRESHOLD));
    }
}
