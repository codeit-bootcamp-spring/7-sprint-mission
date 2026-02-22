package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.enums.Role;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class RoleUpdatedEvent extends ApplicationEvent {
    private UUID userId;
    private Role from;
    private Role to;

    public RoleUpdatedEvent(Object source, UUID userId, Role from, Role to) {
        super(source);
        this.userId = userId;
        this.from = from;
        this.to = to;
    }
}
