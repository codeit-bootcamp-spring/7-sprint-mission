package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.config.OnlineThreshold;
import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.time.Duration;
import java.time.Instant;

public class UserStatus extends BaseEntity {
    User user;
    private OnlineStatus onlineStatus;
    private static final Duration ONLINE_THRESHOLD = OnlineThreshold.ONLINE_THRESHOLD;


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
