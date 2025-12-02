package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.UUID;


public record ChannelPublicCreateRequestDto(


        @NotBlank(message = "Public Channel name")
        String name,

        String description
) {


}
