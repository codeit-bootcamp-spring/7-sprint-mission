package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.enums.ChannelScope;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "read_statuses")
public class ReadStatus extends BaseEntity {
    @ManyToOne
    @JoinColumn(nullable = false, unique = true)
    public User user;

    @ManyToOne
    @JoinColumn(nullable = false, unique = true)
    public Channel channel;

    @Column(nullable = false)
    private Instant lastReadAt;

    @Column(nullable = false)
    private boolean notificationEnabled;

    public ReadStatus(User user, Channel channel) {
        this.user = user;
        this.channel = channel;
        lastReadAt = Instant.now();
        notificationEnabled = channel.getType() == ChannelScope.PRIVATE;
    }

    public void updateLastReadAt(Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
    }

}
