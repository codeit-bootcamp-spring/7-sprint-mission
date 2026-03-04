package com.sprint.mission.discodeit.dto.binarycontent.Response;

import com.sprint.mission.discodeit.entity.BinaryContentStatus;

import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        Long size,
        String contentType,
        BinaryContentStatus status
) {}
