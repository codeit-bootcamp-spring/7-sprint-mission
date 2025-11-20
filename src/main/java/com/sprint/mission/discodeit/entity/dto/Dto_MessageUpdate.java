package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;

public record Dto_MessageUpdate( //all private final
     @NotNull
     String newContent
) {}
