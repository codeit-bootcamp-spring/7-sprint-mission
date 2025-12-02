package com.sprint.mission.discodeit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "read_statuses", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_read_status_user_channel",
                columnNames = {"user_id", "channel_id"}
        )})
public class ReadStatus extends BaseUpdatableEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_read_status_user",
                    foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE"
            )
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "channel_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_read_status_channel",
                    foreignKeyDefinition = "FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE"
            )
    )
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;

    public void update(Instant lastReadAt) {
        if(lastReadAt != null && !lastReadAt.equals(this.lastReadAt)){
            this.lastReadAt = lastReadAt;
        }
    }
}
