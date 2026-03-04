package com.sprint.mission.discodeit.dto.dto_Neo;

import com.sprint.mission.discodeit.entity.Role;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserDto( //all private final
    UUID id,
    String username,
    String email,
    BinaryContentDto profile,
    Boolean online,
    Role role
){
}