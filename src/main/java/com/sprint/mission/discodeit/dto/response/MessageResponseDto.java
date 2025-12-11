package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content, // 메시지
    UUID channelId, // 채널ID
    UserResponseDto author,
    List<BinaryContentResponseDto> attachments // 첨부파일
) {

}

