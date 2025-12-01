package com.sprint.mission.discodeit.dto.binaryContentDto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record BinaryContentCreateRequest(byte @NonNull [] data,
                                         @NonNull String dataName, @NonNull String dataType) {
}