package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_status")
public class UserStatus extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant lastActiveAt;

    @Builder
    public UserStatus(User user) {
        this.user = user;
        this.lastActiveAt = Instant.now();
    }

    public boolean isOnline() {
        return lastActiveAt.isAfter(Instant.now().minusSeconds(300));
    }

    /** 최근 활성 시간 업데이트 */
    public void updateLastSeen() {
        this.lastActiveAt = Instant.now();
    }

    // 기존 서비스에서 호출할 이름을 위해 alias 제공
    public void updateActivity() {
        updateLastSeen();
    }
}
