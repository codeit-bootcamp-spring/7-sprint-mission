package com.sprint.mission.discodeit.user.state;

import com.sprint.mission.discodeit.config.enums.Status;

public record UserStatusRequestDTO(
        Status status,
        String message
) {
}
