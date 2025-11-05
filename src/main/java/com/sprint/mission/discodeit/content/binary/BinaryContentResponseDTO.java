package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.config.enums.ContentOwner;

import java.util.UUID;

public record BinaryContentResponseDTO(
        UUID ownerId,
        ContentOwner owner,
        String fileName,
        String filePath,
        Long fileType

) {
    public static BinaryContentResponseDTO from(BinaryContent content) {
        return new BinaryContentResponseDTO(
            content.getOwnerId(),
            content.getOwner(),
            content.getFileName(),
            content.getFilePath(),
            content.getFileSize()
        );
    }
}
