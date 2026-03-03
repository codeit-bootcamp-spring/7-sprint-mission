package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class RoleUpdatedEvent {
    private UUID receiverId;
    private Role oldRole;
    private Role newRole;
}
