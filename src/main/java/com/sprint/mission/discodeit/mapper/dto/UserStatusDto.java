package com.sprint.mission.discodeit.mapper.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import lombok.Builder;
import java.util.UUID;

@Builder
public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

}
