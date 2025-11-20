package com.sprint.mission.discodeit.entity.dto;

import jakarta.validation.constraints.NotNull;

public record ChannelDto_Update( //all private final
     @NotNull
     String newName,
     @NotNull
     String newDescription
) {}