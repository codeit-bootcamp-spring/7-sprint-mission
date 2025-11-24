package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.dto.Dto_ReadStatusUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false, unique = true)
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;



    public ReadStatus(UUID userId, UUID channelId) {
        super();
        this.userId = userId;
        this.channelId = channelId;
        this.lastReadAt = Instant.now();
    }

    public void updateLastReadAt(Dto_ReadStatusUpdate requestDto) {
        this.lastReadAt = requestDto.newLastReadAt(); // Instant.now();
    }

    @Override
    public String toString() {
        return "ReadStatus = "
            + super.toString()
            + "userId = [" + userId + "]\n"
            + "channelId = [" + channelId + "]\n"
            + "lastReadAt = [" + lastReadAt + "]";
    }
}
