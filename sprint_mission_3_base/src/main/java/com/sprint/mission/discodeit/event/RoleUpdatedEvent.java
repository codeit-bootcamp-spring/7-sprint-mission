package com.sprint.mission.discodeit.event;

import java.util.UUID;

public record RoleUpdatedEvent(UUID targetUserId, String oldRole, String newRole) {
}