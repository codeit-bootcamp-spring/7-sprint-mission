package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.DateTimeUtil;

import java.util.UUID;

public record UserStatusViewRes(
        UUID userId,
        boolean isOnline,
        String lastOnlineAt,
        String lastOfflineAt
) {
    public static UserStatusViewRes from(UserStatus userStatus){
        return new UserStatusViewRes(
                userStatus.getUserId(),
                userStatus.isOnline(),
                DateTimeUtil.format(userStatus.getOnlineAt()),
                DateTimeUtil.format(userStatus.getOfflineAt())
        );
    }
}
