package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.security.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class RoleUpdatedEvent {

    private final Role role;
    private final UUID userId;

}
