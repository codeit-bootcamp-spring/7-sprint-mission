package com.sprint.mission.discodeit.dto.entity.binaryContent.request;

import jakarta.validation.constraints.NotNull;

public record BinaryContentCreateRequest(
        @NotNull(message = "파일 이름은 필수입니다.")
        String fileName,

        @NotNull(message = "컨텐트 타입은 필수입니다.")
        String type,

        @NotNull(message = "파일은 필수입니다.")
        byte[] bytes
) { }
