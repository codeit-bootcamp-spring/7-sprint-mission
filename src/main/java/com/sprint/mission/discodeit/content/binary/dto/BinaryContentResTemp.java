package com.sprint.mission.discodeit.content.binary.dto;

import com.sprint.mission.discodeit.content.binary.BinaryContent;
import java.time.Instant;
import java.util.UUID;

public record BinaryContentResTemp(
    UUID id,
    Instant createdAt,
    String fileName,
    Long size,
    String contentType,
    String bytes
) {

  public static BinaryContentResTemp from(BinaryContent content) {
    return new BinaryContentResTemp(
        content.getId(),
        content.getCreatedAt(),
        content.getFileName(),
        content.getFileSize(),
        content.getFileType(),
        content.getBytes());
  }
}
