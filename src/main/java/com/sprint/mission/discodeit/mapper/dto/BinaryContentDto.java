package com.sprint.mission.discodeit.mapper.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType
//    byte[] bytes
) {
    public static BinaryContentDto from(BinaryContent binaryContent) {
        return new BinaryContentDto(
            binaryContent.getId(),
            binaryContent.getFileName(),
            binaryContent.getSize(),
            binaryContent.getContentType()
        );
    }

}
