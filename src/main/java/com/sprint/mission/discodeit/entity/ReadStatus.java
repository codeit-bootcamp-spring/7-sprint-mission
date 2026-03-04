package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@Entity
@Table(name = "read_statuses", uniqueConstraints = {
        @UniqueConstraint(
                name="read_statuses_user_id_channel_id_key",
                columnNames={"user_id","channel_id"}
        )})
@NoArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

    @JoinColumn(name = "user_id",  nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "channel_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;

    @Column(nullable = false)
    private Instant lastReadAt;

    @Column(nullable = false)
    private boolean notificationEnabled;

    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
        this.lastReadAt = Instant.now();
        notificationEnabled = channel.getType() == ChannelType.PRIVATE;
    }

    public void updateReadStatus(Instant newLastReadAt) {
        lastReadAt = newLastReadAt;
    }
    public void updateNotificationEnabled(boolean notificationEnabled) {
        this.notificationEnabled = notificationEnabled;
    }
}
