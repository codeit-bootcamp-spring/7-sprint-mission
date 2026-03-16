package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.service.dto.response.UserDto;

public record UserCreatedEvent(
        UserDto userDto
) {
}