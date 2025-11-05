package com.sprint.mission.discodeit.entity.dto;

import java.util.List;
import java.util.Optional;

public record Dto_MessageCreate(
        Dto_Message dtoMessage,
        Optional<List<Dto_BinaryContent>> contentList
) {

}
