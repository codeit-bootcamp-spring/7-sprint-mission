package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "파일 데이터 응답 DTO")
public record BinaryContentDto(
    @Schema(description = "파일 ID", example = "b79e7d1a-91e3-45af-8c9f-08e52d76cc31")
    UUID id,

    @Schema(description = "파일 이름", example = "image.png")
    String fileName,

    @Schema(description = "MIME 타입", example = "image/png")
    String contentType
) {

  public static BinaryContentDto from(BinaryContent entity) {
    return new BinaryContentDto(
        entity.getId(),
        entity.getFileName(),
        entity.getContentType()
    );
  }
}
