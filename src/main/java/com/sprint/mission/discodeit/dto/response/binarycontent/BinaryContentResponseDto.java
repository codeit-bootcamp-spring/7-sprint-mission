package com.sprint.mission.discodeit.dto.response.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentResponseDto(
        UUID id,
        Instant createdAt,
        String fileName,
        String contentType,
        byte[] data) {

    public static BinaryContentResponseDto from(BinaryContent bc) {
        return new BinaryContentResponseDto(
                bc.getId(),
                bc.getCreatedAt(),
                bc.getFileName(),
                bc.getContentType(),
                bc.getData()
        );
    }
}
