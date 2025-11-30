package com.sprint.mission.discodeit.dto.userStatus.response;

import java.util.UUID;

public record UserStatusViewRes(
    UUID userId,
    boolean isOnline,
    String lastOfflineAt
) {

}
