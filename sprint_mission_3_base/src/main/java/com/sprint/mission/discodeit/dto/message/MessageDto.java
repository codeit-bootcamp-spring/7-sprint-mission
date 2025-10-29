package com.sprint.mission.discodeit.dto.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String content,
        UUID authorId,
        UUID channelId,
        List<?> attachments   // ← 핵심: 와일드카드(or List<Object>)
) {}
