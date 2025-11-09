package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.Optional;

public record Dto_MessageCreate( //all private final
        @NotBlank(message = "dtoMessage is mandatory")
        Dto_Message dtoMessage,
        Optional<List<Dto_BinaryContent>> contentList
) {

}
