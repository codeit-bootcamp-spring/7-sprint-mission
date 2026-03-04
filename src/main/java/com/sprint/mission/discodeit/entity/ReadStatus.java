package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.dto_Neo.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "read_statuses",
    uniqueConstraints = @UniqueConstraint(
        name = "read_statuses_user_id_N_channel_id_uk",
        columnNames = {"user_id", "channel_id"}
    ))
@Getter // @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false) // , unique = true
    private Channel channel;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;

    @Column(name = "notification_enabled", nullable = false)
    boolean notificationEnabled = false; // private channel = true/ public channel = false

    public void update(ReadStatusUpdateRequest requestDto) {
        if (requestDto.newLastReadAt() != null) {
            this.lastReadAt = requestDto.newLastReadAt();
        }
        if (requestDto.newNotificationEnabled() != null) {
            this.notificationEnabled = requestDto.newNotificationEnabled();
        }
    }
}
