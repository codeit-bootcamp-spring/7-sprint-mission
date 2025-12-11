package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotNull;

public record ChannelDto_Update( //all private final
     @NotNull
     String newName,
     @NotNull
     String newDescription
) {}