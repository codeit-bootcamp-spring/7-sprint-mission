package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor
public class UserStatus extends BaseEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private Instant lastActiveAt;

    // online 계산: lastActiveAt ~ now < 임계값으로 서비스단에서 제공

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant newLastActiveAt) {
        this.lastActiveAt = newLastActiveAt;
    }
}
