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
@Table(name = "read_status")
public class ReadStatus extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private Instant lastReadAt;

    @Builder
    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = Instant.now();
    }

    /** 마지막 읽은 시각 업데이트 */
    public void updateReadTime() {
        this.lastReadAt = Instant.now();
    }
}
