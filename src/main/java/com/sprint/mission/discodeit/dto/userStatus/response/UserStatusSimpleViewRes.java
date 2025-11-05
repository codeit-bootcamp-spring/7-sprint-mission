package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;

public record UserStatusSimpleViewRes(
        boolean isOnline
) {
    public static UserStatusSimpleViewRes from(UserStatus userStatus){
        return new UserStatusSimpleViewRes(userStatus.isOnline());
    }
}
