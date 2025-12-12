package com.sprint.mission.discodeit.dto.message.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateMessageRequestDto(
        @NotBlank(message = "메시지 내용은 비어 있을 수 없습니다.")
        String newContent
) { }
