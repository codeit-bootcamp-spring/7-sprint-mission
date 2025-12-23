package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID channelId,
        UserResponseDto author,
        List<BinaryContentResponseDto> attachments
) {

}
