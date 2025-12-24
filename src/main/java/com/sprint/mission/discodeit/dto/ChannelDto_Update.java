package com.sprint.mission.discodeit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChannelDto_Update( //all private final
     @NotBlank
     String newName,
     @NotBlank
     String newDescription
) {}