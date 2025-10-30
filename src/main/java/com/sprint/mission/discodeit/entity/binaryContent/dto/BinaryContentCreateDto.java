package com.sprint.mission.discodeit.entity.binaryContent.dto;

import com.sprint.mission.discodeit.entity.binaryContent.BinaryContent;
import lombok.Builder;
import lombok.NonNull;

import java.util.UUID;

@Builder
public record BinaryContentCreateDto(@NonNull UUID userId, UUID messageId, byte @NonNull [] data,
                                     @NonNull String dataName, @NonNull String dataType) {
}
// messageId = null ? profileImage : attachment