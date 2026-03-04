package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.enums.Role;
import lombok.Getter;

import java.util.UUID;

@Getter
public class RoleUpdatedEvent {
    private UUID userId;
    private Role from;
    private Role to;

    public RoleUpdatedEvent(UUID userId, Role from, Role to) {
        this.userId = userId;
        this.from = from;
        this.to = to;
    }
}
