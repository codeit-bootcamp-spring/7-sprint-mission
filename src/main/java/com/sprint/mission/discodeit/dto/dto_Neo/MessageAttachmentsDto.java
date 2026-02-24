package com.sprint.mission.discodeit.dto.dto_Neo;

import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageAttachmentsDto(
    UUID id,
    UUID messageId,
    UUID attachmentId
) {}
