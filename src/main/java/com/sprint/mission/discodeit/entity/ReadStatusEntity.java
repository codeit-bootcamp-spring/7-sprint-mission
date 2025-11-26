package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;


@Getter
@Entity
@Setter
@Table(name = "read_statuses")
public class ReadStatusEntity extends BaseUpdatableEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private ChannelEntity channelId;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;


}

