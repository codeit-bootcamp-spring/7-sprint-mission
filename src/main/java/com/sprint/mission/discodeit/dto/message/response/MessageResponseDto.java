package com.sprint.mission.discodeit.dto.message.response;

import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
    List<BinaryContent> attachments
) {

}
