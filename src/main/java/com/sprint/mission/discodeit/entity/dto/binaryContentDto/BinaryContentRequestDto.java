package com.sprint.mission.discodeit.entity.dto.binaryContentDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record BinaryContentRequestDto(byte @NonNull [] data,
                                      @NonNull String dataName, @NonNull String dataType) {
}