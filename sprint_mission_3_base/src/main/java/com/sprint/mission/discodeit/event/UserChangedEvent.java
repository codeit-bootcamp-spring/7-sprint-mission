package com.sprint.mission.discodeit.event;

public record UserChangedEvent(
        String eventName,
        Object user
) {
}