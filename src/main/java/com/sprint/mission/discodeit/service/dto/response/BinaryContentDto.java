package com.sprint.mission.discodeit.service.dto.response;

import com.sprint.mission.discodeit.domain.BinaryContent;

import java.util.UUID;

public record BinaryContentDto(
        UUID id,
        String fileName,
        long size,
        String contentType


) {
}
