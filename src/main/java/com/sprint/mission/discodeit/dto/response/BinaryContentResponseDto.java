package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record BinaryContentResponseDto(
    UUID id,
    String fileName,
    Long size,
    String contentType,
    byte[] bytes//표준 데이터
) {

  public static BinaryContentResponseDto from(BinaryContent binaryContent) {
    return new BinaryContentResponseDto(
        binaryContent.getId(),
        binaryContent.getFileName(),
        binaryContent.getSize(),
        binaryContent.getContentType(),
        binaryContent.getBytes()
    );
  }
}
