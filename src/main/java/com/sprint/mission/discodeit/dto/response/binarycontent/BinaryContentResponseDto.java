package com.sprint.mission.discodeit.dto.response.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.Base64;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        String contentType,
        String bytes,
        Long size
) {}
