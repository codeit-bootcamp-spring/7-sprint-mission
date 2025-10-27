package com.sprint.mission.discodeit.entity.base;

import com.sprint.mission.discodeit.enums.OnlineStatus;

import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.enums.OnlineStatus.*;

public class UserStatus extends BaseEntity {
    public User user;
//    public OnlineStatus onlineStatus = OFFLINE;

    public UserStatus(User user) {
        this.user = user;
    }

//    public void setOnline() {
//        onlineStatus = ONLINE;
//    }
//
//    public void setOffline() {
//        onlineStatus = OFFLINE;
//    }
//
//    public void setDoNotDisturb() {
//        onlineStatus = DO_NOT_DISTURB;
//    }
//
//    public void setAway() {
//        onlineStatus = AWAY;
//    }



    public boolean isOnline() {
        return this.updatedAt.isAfter(Instant.now().minusSeconds(60 * 5));
    }
}
