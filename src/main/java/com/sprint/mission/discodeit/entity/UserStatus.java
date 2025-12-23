package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Getter @ToString(exclude = "user")
@Entity
@Table(name = "user_statuses")
@NoArgsConstructor
public class UserStatus extends BaseUpdatableEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",  nullable = false, unique = true)
    private User user;

    private Instant lastActiveAt;

    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public void updateLastActiveAt(Instant lastActiveAt){
        this.lastActiveAt = lastActiveAt;
    }

    // 5분이 안지났으면 온라인
    public boolean isOnline() {
        Instant expirationTime = lastActiveAt.plusSeconds(300);
        // 만료시간(업데이트시간 + 5분)이 현재시간보다 뒤에 있는가? ture : false
        return expirationTime.isAfter(Instant.now());
    }

}

