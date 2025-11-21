package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.Instant;

@Getter @ToString
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

    public ReadStatus(User user, Channel channel) {
        super();
        this.user = user;
        this.channel = channel;
        this.lastReadAt = Instant.now();
    }

    public void updateReadStatus() {
        lastReadAt = Instant.now();
    }
    public void updateReadStatus(Instant newLastReadAt) {
        lastReadAt = newLastReadAt;
    }
}
