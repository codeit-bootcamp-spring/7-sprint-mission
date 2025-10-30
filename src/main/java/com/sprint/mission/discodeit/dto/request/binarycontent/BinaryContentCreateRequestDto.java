package com.sprint.mission.discodeit.dto.request.binarycontent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record BinaryContentCreateRequestDto(
        UUID id,
        Instant createdAt,
        @NotBlank(message = "파일 이름이 필요합니다.")
        String fileName,

        @NotBlank(message = "내용을 입력해주세요.")
        String contentType,

        @NotNull
        byte[] data) {
}
