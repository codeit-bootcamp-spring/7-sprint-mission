package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_statuses")
@AttributeOverride(name = "id", column = @Column(name = "user_status_id"))
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "last_active_at")
    private Instant lastActiveAt;

    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt=Instant.now();
    }

    public void updateLastActiveAt(Instant lastActiveAt){
        this.lastActiveAt=lastActiveAt;
    }
}
