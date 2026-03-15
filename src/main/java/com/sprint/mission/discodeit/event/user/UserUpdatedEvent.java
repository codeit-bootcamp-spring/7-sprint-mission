package com.sprint.mission.discodeit.event.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserUpdatedEvent {
    private final UUID userId;

}
