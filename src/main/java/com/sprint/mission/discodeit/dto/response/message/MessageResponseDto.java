package com.sprint.mission.discodeit.dto.response.message;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
        UUID id,
        String content,
        UserResponseDto author,
        UUID channelId,
        List<BinaryContentResponseDto> attachments,
        Instant createdAt,
        Instant updatedAt) {
}
