package com.sprint.mission.discodeit.dto.request.message;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageUpdateRequestDto(
        @NotBlank
        @Size(max = 1000)
        String newContent) {
}
