package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.common.config.OnlineThreshold;
import com.sprint.mission.discodeit.enums.OnlineStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
@AllArgsConstructor
public class UserStatus extends BaseEntity {
    private User user;
    @Setter
    private Instant lastActiveAt;
    @Setter
    private OnlineStatus onlineStatus = OnlineStatus.OFFLINE;
    private static final Duration ONLINE_THRESHOLD = OnlineThreshold.ONLINE_THRESHOLD;

    public UserStatus(User user) {
        this.user = user;
    }

    public OnlineStatus getOnlineStatus() {
        if (onlineStatus == OnlineStatus.ONLINE || onlineStatus == OnlineStatus.AWAY) {
            if (this.lastActiveAt.isAfter(Instant.now().minus(ONLINE_THRESHOLD))) {
                onlineStatus = OnlineStatus.ONLINE;
            } else {
                onlineStatus = OnlineStatus.AWAY;
            }
        }
        return onlineStatus;
    }

    // 파일 IO시 필드 복원을 위한 메서드
    public static UserStatus fromDto(java.util.UUID uuid, Instant createdAt, Instant updatedAt,
                                     User user, Instant lastActiveAt, OnlineStatus onlineStatus) {
        UserStatus userStatus = new UserStatus(user, lastActiveAt, onlineStatus);
        userStatus.uuid = uuid;
        userStatus.createdAt = createdAt;
        userStatus.updatedAt = updatedAt;
        return userStatus;
    }
}
