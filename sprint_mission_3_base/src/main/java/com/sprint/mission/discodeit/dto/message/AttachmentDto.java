package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record AttachmentDto(
        UUID id,
        String filename,
        String contentType
) {}
