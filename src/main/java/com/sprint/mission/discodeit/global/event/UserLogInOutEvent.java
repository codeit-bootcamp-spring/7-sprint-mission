package com.sprint.mission.discodeit.global.event;

import java.util.UUID;

public record UserLogInOutEvent(
        UUID userId,
        boolean loggedIn
) {
}
