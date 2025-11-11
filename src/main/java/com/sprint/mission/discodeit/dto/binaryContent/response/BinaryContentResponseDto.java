package com.sprint.mission.discodeit.dto.binaryContent.response;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BinaryContentResponseDto(
        UUID binaryContentID,
        String fileName,
        String contentType,
        byte[] binaryContent
) {
    public static BinaryContentResponseDto from(BinaryContent binaryContent) {
        return BinaryContentResponseDto.builder()
                .binaryContentID(binaryContent.getId())
                .fileName(binaryContent.getFileName())
                .contentType(binaryContent.getContentType())
                .binaryContent(binaryContent.getBinaryContent())
                .build();
    }
}
