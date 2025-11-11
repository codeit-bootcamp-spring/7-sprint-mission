package com.sprint.mission.discodeit.dto.binaryContent.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record BinaryContentResponseDto(
        UUID id,
        Instant createdAt,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return BinaryContentResponseDto.builder()
                .id(binaryContent.getId())
                .fileName(binaryContent.getFileName())
                .contentType(binaryContent.getContentType())
                .bytes(binaryContent.getBytes())
                .build();
    }
}
