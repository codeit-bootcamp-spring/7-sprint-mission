package com.sprint.mission.discodeit.content.binary.dto;

import com.sprint.mission.discodeit.config.enums.ContentOwner;

import com.sprint.mission.discodeit.content.binary.BinaryContent;
import java.util.UUID;

public record BinaryContentResponse(
    UUID id,
    UUID ownerId,
    ContentOwner owner,
    String fileName,
    String filePath,
    Long fileType

) {

  public static BinaryContentResponse from(BinaryContent content) {
    return new BinaryContentResponse(
        content.getId(),
        content.getOwnerId(),
        content.getOwner(),
        content.getFileName(),
        content.getFilePath(),
        content.getFileSize()
    );
  }
}
