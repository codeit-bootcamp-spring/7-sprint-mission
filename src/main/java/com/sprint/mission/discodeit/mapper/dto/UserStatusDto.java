package com.sprint.mission.discodeit.mapper.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import lombok.Builder;

@Builder
public record UserStatusDto(
    User user,
    Instant lastActiveAt
) {

}
