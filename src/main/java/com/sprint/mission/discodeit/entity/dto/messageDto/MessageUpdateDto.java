package com.sprint.mission.discodeit.entity.dto.messageDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record MessageUpdateDto(String newContent) {
}
