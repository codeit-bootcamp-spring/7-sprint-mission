package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record BinaryContentResponseDto(
        UUID id,
        String fileName,
        Long size,
        String contentType
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent){
        return BinaryContentResponseDto.builder()
                .id(binaryContent.getId())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getSize())
                .contentType(binaryContent.getContentType())

                .build();
    }
}
