package com.sprint.mission.discodeit.dto.binaryContent.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Optional;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record CreateBinaryContentDto(
    @NotBlank(message = "파일 이름은 필수입니다.")
    String fileName,

    @NotBlank(message = "ContentType은 필수입니다.")
    String contentType,

    Long size,

    @NotNull(message = "파일은 필수입니다.")
    byte[] bytes
) {

  public static Optional<CreateBinaryContentDto> of(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return Optional.empty();
    } else {
      try {
        CreateBinaryContentDto CreateBinaryContentDto = new CreateBinaryContentDto(
            file.getOriginalFilename(),
            file.getContentType(),
            file.getSize(),
            file.getBytes()
        );
        return Optional.of(CreateBinaryContentDto);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

  }
}
