package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
/*
    사용자가 채널 별 마지막으로 메세지를 읽은 시간을 표현하는 도메인 모델.
    사용자별 각 채널에 읽지 않은 메세지를 확인하기 위해 활용
 */
@Getter
@ToString
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;

    @Column(name = "notification_enabled", nullable = false)
    private boolean notificationEnabled;

    protected ReadStatus() {}

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.user = Objects.requireNonNull(user);
        this.channel = Objects.requireNonNull(channel);
        this.lastReadAt = Objects.requireNonNull(lastReadAt);
        this.notificationEnabled = channel.getType() == ChannelType.PRIVATE;
    }

    public void readNow() {
        this.lastReadAt = Instant.now();
    }

    public void readAt(Instant at) {
        Instant time = Objects.requireNonNull(at);
        if(time.isAfter(this.lastReadAt)) {
            this.lastReadAt = time;
        }
    }

    public void updateNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
}
