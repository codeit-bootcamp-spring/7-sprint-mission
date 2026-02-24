package com.sprint.mission.discodeit.dto.dto_Neo;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import java.util.UUID;
import lombok.Builder;

@Builder
public record BinaryContentDto(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    BinaryContentStatus status
//    byte[] bytes
) {
    public static BinaryContentDto from(BinaryContent binaryContent) {
        return new BinaryContentDto(
            binaryContent.getId(),
            binaryContent.getFileName(),
            binaryContent.getSize(),
            binaryContent.getContentType(),
            binaryContent.getStatus()
        );
    }
}
