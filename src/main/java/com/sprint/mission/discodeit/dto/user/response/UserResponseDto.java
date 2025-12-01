package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    String username,
    String email,
    BinaryContentResponseDto profile,
    Boolean online
) {

}
