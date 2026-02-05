package com.sprint.mission.discodeit.dto.message;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MessageUpdateRequestDto(
        @NotBlank String newContent
) {
}
