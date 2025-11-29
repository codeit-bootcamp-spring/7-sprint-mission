package com.sprint.mission.discodeit.mapper.dto;

import java.util.UUID;
import lombok.Builder;

@Builder
public record MessageAttachmentsDto(
    UUID id,
    UUID messageId,
    UUID attachmentId
) {}
