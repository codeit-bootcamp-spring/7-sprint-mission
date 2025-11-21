package com.sprint.mission.discodeit.dto.response.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BinaryContentDto(
        UUID id,
        String fileName,
        Long size,
        String contentType,
        byte[] bytes
) {
    public static BinaryContentDto from(BinaryContent binaryContent){
        return BinaryContentDto.builder()
                .id(binaryContent.getId())
                .fileName(binaryContent.getFileName())
                .size(binaryContent.getSize())
                .contentType(binaryContent.getContentType())

                .build();
    }
}
