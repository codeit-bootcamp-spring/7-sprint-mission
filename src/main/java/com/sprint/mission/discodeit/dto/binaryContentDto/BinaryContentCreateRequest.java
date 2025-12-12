package com.sprint.mission.discodeit.dto.binaryContentDto;

import lombok.Builder;

@Builder
public record BinaryContentCreateRequest(
        byte[] data,
        String dataName,
        String dataType) {
}