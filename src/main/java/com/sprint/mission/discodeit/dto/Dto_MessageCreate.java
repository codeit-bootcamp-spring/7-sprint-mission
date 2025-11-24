package com.sprint.mission.discodeit.dto;

import java.util.List;
import java.util.Optional;

public record Dto_MessageCreate( //all private final
        //@NotBlank(message = "dtoMessage is mandatory")
        MessageCreateRequest dtoMessage,
        Optional<List<Dto_BinaryContent>> contentList
) {}
