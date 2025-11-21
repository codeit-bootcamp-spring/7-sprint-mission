package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "read_statuses")
public class ReadStatusEntity extends BaseUpdatableEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ChannelEntity channelId;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;
}

