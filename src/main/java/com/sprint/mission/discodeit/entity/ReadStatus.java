package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "read_statuses")
@AttributeOverride(name = "id", column = @Column(name = "read_status_id"))
public class ReadStatus extends BaseUpdatableEntity {
    //user_id, channel_id 복합 유니크 제약 조건 걺

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "last_read_at")
    private Instant lastReadAt;

    @Column(name = "notification_enabled", nullable = false)
    private boolean notificationEnabled;

    public ReadStatus(User user, Channel channel, boolean notificationEnabled) {
        this.user = user;
        this.channel = channel;
        this.notificationEnabled=notificationEnabled;
    }

    public ReadStatus(User user, Channel channel, Instant lastReadAt, boolean notificationEnabled) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = lastReadAt;
        this.notificationEnabled=notificationEnabled;
    }

    public void updateLastReadAt(Instant lastReadAt){
        this.lastReadAt=lastReadAt;
    }

    public void updateNotificationEnabled(boolean notificationEnabled){
        this.notificationEnabled=notificationEnabled;
    }
}

