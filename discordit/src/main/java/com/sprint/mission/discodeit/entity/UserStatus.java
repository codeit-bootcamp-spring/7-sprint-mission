package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.config.OnlineThreshold;
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
    private OnlineStatus onlineStatus = OnlineStatus.OFFLINE;
    private static final Duration ONLINE_THRESHOLD = OnlineThreshold.ONLINE_THRESHOLD;

    public UserStatus(User user) {
        this.user = user;
    }

    public OnlineStatus getOnlineStatus() {
        if (onlineStatus == OnlineStatus.ONLINE || onlineStatus == OnlineStatus.AWAY) {
            if (user.updatedAt.isAfter(Instant.now().minus(ONLINE_THRESHOLD))) {
                onlineStatus = OnlineStatus.ONLINE;
            } else {
                onlineStatus = OnlineStatus.AWAY;
            }
        }
        return onlineStatus;
    }
}
