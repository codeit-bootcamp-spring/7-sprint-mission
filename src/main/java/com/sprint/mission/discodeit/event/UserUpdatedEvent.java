package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.security.Role;

import java.util.UUID;

public record UserUpdatedEvent(
        UserDto userDto,
        String eventName
) {
}
