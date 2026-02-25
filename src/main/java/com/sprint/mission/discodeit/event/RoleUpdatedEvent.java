package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.type.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class RoleUpdatedEvent {
    private final UUID receiverId;
    private final Role newRole;
    private final Role oldRole;
}
