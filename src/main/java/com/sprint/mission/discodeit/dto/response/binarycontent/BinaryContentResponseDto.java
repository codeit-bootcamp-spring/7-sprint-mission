package com.sprint.mission.discodeit.dto.response.binarycontent;

import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        String contentType,
        Long size
) {}
