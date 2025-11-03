package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public record Dto_ReadStatusUpdate(
        UUID id,
        Instant readAt
) {
    public static Dto_ReadStatusUpdate from(UUID readStatusID) {
        return Dto_ReadStatusUpdate.builder()
                .id(readStatusID)
                .readAt(Instant.now())
                .build();
    }
}
