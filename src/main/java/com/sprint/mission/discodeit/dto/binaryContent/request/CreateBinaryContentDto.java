package com.sprint.mission.discodeit.dto.binaryContent.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBinaryContentDto(
        @NotBlank(message = "파일 이름은 필수입니다.")
        String fileName,

        @NotBlank(message = "ContentType은 필수입니다.")
        String contentType,

        Long size,

        @NotNull(message = "파일은 필수입니다.")
        byte[] bytes
) {
}
