package com.sprint.mission.discodeit.entity.dto;

import lombok.Builder;

import java.util.UUID;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public record Dto_ReadStatusUpdate(
        UUID readStatusID
) {
    public static Dto_ReadStatusUpdate from(UUID readStatusID) {
        return Dto_ReadStatusUpdate.builder()
                .readStatusID(readStatusID)
                .build();
    }
}
