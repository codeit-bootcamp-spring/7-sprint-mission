package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.UUID;


public record Res_BinaryContent( //all private final
        //@NotBlank(message = "binaryContentId is mandatory")
        UUID binaryContentId,
        String fileName,
        String contentType,
        byte[] bytes,
        Long fileSize
) {
  public static Res_BinaryContent from(BinaryContent binaryContent) {
      return new Res_BinaryContent(
          binaryContent.getId(),
          binaryContent.getFileName(),
          binaryContent.getContentType(),
          binaryContent.getBytes(),
          binaryContent.getSize()
      );
    }
}
