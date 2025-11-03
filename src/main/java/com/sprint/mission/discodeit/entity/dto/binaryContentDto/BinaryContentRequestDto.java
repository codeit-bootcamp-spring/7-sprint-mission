package com.sprint.mission.discodeit.entity.dto.binaryContentDto;

import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record BinaryContentRequestDto(@NonNull UUID userId, UUID messageId, byte @NonNull [] data,
                                      @NonNull String dataName, @NonNull String dataType) {
}
// messageId = null ? profileImage : attachment