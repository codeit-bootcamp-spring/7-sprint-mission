package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.config.enums.ContentOwner;

import java.util.UUID;

public record BinaryContentDTO(
        UUID ownerId,
        ContentOwner owner,
        String fileName,
        String filePath,
        Long fileType

) {
    public static BinaryContentDTO from(BinaryContent content) {
        return new BinaryContentDTO(
            content.getOwnerId(),
            content.getOwner(),
            content.getFileName(),
            content.getFilePath(),
            content.getFileSize()
        );
    }
}
