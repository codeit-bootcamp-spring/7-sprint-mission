package com.sprint.mission.discodeit.dto.binaryContent.request;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.UUID;

public record BinaryContentGetRequest(
        @NotEmpty(message = "하나 이상의 아이디는 필수입니다.")
        List<UUID> ids
) {
}
