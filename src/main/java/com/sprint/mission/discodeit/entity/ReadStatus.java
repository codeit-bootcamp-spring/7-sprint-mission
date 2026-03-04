package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusCreateNotAllowedException;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Entity
@Table(
        name = "read_statuses",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id", "channel_id"}
        )
)
public class ReadStatus extends BaseUpdatableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
    @CreatedDate
    private Instant lastReadAt;

    @Column(name = "notification_enabled", nullable = false)
    private boolean notificationEnabled;

    @Builder
    public ReadStatus(User user, Channel channel, boolean notificationEnabled) {
//        if (channel.getType() != ChannelType.PRIVATE) {
//            throw new ReadStatusCreateNotAllowedException(channel.getType());
//        }
        this.user = user;
        this.channel = channel;
        this.notificationEnabled = notificationEnabled;
    }

    public boolean applyPatch(Instant readAt, Boolean notificationEnabled) {
        boolean isChanged = false;
        isChanged |= updateReadAt(readAt);
        isChanged |= updateNotificationEnabled(notificationEnabled);
        return isChanged;
    }


    public boolean updateNotificationEnabled(Boolean notificationEnabled) {
        if (notificationEnabled == null) {
            return false;
        }
        if (this.notificationEnabled == notificationEnabled) {
            return false;
        }
        this.notificationEnabled = notificationEnabled;
        return true;
    }

    public boolean updateReadAt(Instant readAt) {

        if (readAt == null) {
            throw new DiscodeitException(ErrorCode.INVALID_INPUT);
        }
        // 최초이면 무조건 세팅
        if (this.lastReadAt == null) {
            this.lastReadAt = readAt;
            return true;
        }
        // 이후엔 더 미래일 때만 변경
        if (readAt.isAfter(this.lastReadAt)) {
            this.lastReadAt = readAt;
            return true;
        }

        return false;
    }


}
