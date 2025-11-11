package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto (
        UUID userStatusId,
        UUID userId, //유저 ID
        Instant lastAccessTime, //마지막 접속 시간
        boolean isOnline
) {
    public static UserStatusResponseDto from(UserStatus userStatus) {
        return new UserStatusResponseDto(
                userStatus.getId(),
                userStatus.getUserId(),
                userStatus.getLastAccessTime(),
                userStatus.isOnline()
        );
    }
}

