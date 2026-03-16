package com.sprint.mission.discodeit.sse;

import com.sprint.mission.discodeit.service.dto.response.UserDto;

public record UserDeletedEvent(
        UserDto userDto
) {
}