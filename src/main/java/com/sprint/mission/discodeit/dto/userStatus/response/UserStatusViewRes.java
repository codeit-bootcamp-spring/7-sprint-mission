package com.sprint.mission.discodeit.dto.userStatus.response;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import lombok.Builder;

import java.util.UUID;

@Builder
public record UserStatusViewRes(
        UUID userId,
        boolean isOnline,
        String lastOnlineAt,
        String lastOfflineAt
) {
    public static UserStatusViewRes from(UserStatus userStatus){
        return UserStatusViewRes.builder()
                .userId(userStatus.getUserId())
                .isOnline(userStatus.isOnline())
                .lastOnlineAt(DateTimeUtil.format(userStatus.getOnlineAt()))
                .lastOfflineAt(DateTimeUtil.format(userStatus.getOfflineAt()))
                .build();
    }
}
