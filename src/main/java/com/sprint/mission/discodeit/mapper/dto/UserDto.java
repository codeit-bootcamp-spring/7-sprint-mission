package com.sprint.mission.discodeit.mapper.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto( //all private final
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online
){
}