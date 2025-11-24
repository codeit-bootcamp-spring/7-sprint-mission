package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "read_statuses")
public class ReadStatusEntity extends BaseUpdatableEntity{

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "channel_id", nullable = false)
    private UUID channelId;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;
}

