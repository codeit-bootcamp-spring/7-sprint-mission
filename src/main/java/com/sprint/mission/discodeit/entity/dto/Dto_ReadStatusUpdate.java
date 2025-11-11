package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.UUID;

//@Getter
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public record Dto_ReadStatusUpdate( //all private final
        @NotBlank(message = "readStatusID is mandatory")
        UUID readStatusID
) {
    public static Dto_ReadStatusUpdate from(UUID readStatusID) {
        return Dto_ReadStatusUpdate.builder()
                .readStatusID(readStatusID)
                .build();
    }
}
